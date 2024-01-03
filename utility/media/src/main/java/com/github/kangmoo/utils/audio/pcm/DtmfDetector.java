package com.github.kangmoo.utils.audio.pcm;

import lombok.Getter;
import lombok.Synchronized;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author kangmoo Heo
 */
@Getter
public class DtmfDetector {
    private static final double CUT_OFF_POWER = 0.004;
    private static final double FFT_CUT_OFF_POWER_NOISE_RATIO = 0.46;
    private static final double FFT_FRAME_DURATION = 0.030;

    /* DTMF 키패드 매핑 테이블
    | Freq   | 1209 Hz | 1336 Hz | 1477 Hz | 1633 Hz |
    |--------|---------|---------|---------|---------|
    | 697 Hz |    1    |    2    |    3    |    A    |
    | 770 Hz |    4    |    5    |    6    |    B    |
    | 852 Hz |    7    |    8    |    9    |    C    |
    | 941 Hz |    *    |    0    |    #    |    D    |
     */
    private static final char[][] DTMF_KEYPAD_MAPPING = {
            {'1', '2', '3', 'A'},  // 697 Hz
            {'4', '5', '6', 'B'},  // 770 Hz
            {'7', '8', '9', 'C'},  // 852 Hz
            {'*', '0', '#', 'D'}  // 941 Hz
    };

    /**
     * The list of valid DTMF frequencies that are going to be processed and searched for within the ITU-T recommendations . See the <a href="http://en.wikipedia.org/wiki/Dual-tone_multi-frequency_signaling" > WikiPedia article on DTMF</a>.
     */
    private static final int[] DTMF_FREQUENCIES_BIN = {
            687, 697, 707, // 697
            758, 770, 782, // 770
            839, 852, 865, // 852
            927, 941, 955, // 941
            1191, 1209, 1227, // 1209
            1316, 1336, 1356, // 1336
            1455, 1477, 1499, // 1477
            1609, 1633, 1647, 1657 // 1633
    };

    private final int bitDepth;
    private final int sampleRate;
    private final int frameSize;
    private final int[] freqIndices;
    private final int bufferSize;
    private final char[] prev = new char[]{'_', '_'};
    private final StringBuilder dtmfStr = new StringBuilder();

    public DtmfDetector(int bitDepth, int sampleRate) {
        this.sampleRate = sampleRate;
        this.bitDepth = bitDepth;
        this.frameSize = getFrameSize(sampleRate);
        this.freqIndices = getCentreIndices(sampleRate, frameSize);
        this.bufferSize = (int) Math.ceil(frameSize / 3.0);
    }

    @Synchronized
    public void dtmfClear() {
        prev[0] = '_';
        prev[1] = '_';
        dtmfStr.setLength(0);
    }

    @Synchronized
    public void put(byte[] data) {
        double[] data2 = convertBytesToSamples(data, bitDepth);
        List<double[]> frames = new ArrayList<>();
        for (int i = 0; i + bufferSize < data2.length; i += bufferSize) {
            frames.add(Arrays.copyOfRange(data2, i, i + bufferSize));
        }

        for (double[] frame : frames) {
            char curr = decodeDtmfFrame(frame, frameSize, freqIndices).orElse('_');
            if (curr != '_' && curr == prev[0] && curr != prev[1]) {
                dtmfStr.append(curr);
            }
            prev[1] = prev[0];
            prev[0] = curr;
        }
    }

    public String getDtmf() {
        return dtmfStr.toString();
    }

    /**
     * Convert PCM Byte Data to double[]
     *
     * @param bytes    PCM Byte Data
     * @param bitDepth Bit depth (예: 8, 16, 24)
     * @return 오디오 샘플의 double 배열
     * @throws IllegalArgumentException 유효하지 않은 입력에 대한 예외
     */
    public static double[] convertBytesToSamples(byte[] bytes, int bitDepth) throws IllegalArgumentException {
        // Check bit depth
        if (bitDepth != 8 && bitDepth != 16 && bitDepth != 24) {
            throw new IllegalArgumentException("Unsupported bit depth: " + bitDepth);
        }

        int bytesPerSample = bitDepth / 8;

        if (bytes.length % bytesPerSample != 0) {
            throw new IllegalArgumentException("Byte array length is not compatible with bit depth.");
        }

        double[] samples = new double[bytes.length / bytesPerSample];

        for (int i = 0; i < samples.length; i++) {
            long sample = 0;
            for (int byteIndex = 0; byteIndex < bytesPerSample; byteIndex++) {
                sample |= (bytes[i * bytesPerSample + byteIndex] & 0xFF) << (byteIndex * 8);
            }

            // 16비트 이상의 경우, 부호 있는 값으로 변환
            if (bitDepth > 8) {
                sample = (sample << (64 - bitDepth)) >> (64 - bitDepth);
            }

            // 샘플을 double 형태로 정규화
            samples[i] = sample / Math.pow(2, bitDepth - 1.0);
        }
        return samples;
    }


    /**
     * Method to generate a frequency spectrum of the frame using FFT
     *
     * @param frame Frame to be transformed
     * @return an Array showing the relative powers of all frequencies
     */
    private static double[] transformFrameFFT(double[] frame) {
        return Arrays.stream(new FastFourierTransformer(DftNormalization.STANDARD).transform(frame, TransformType.FORWARD))
                .map(complex -> complex.multiply(complex).abs())
                .limit(frame.length / 2 + 1L)
                .mapToDouble(Double::doubleValue)
                .toArray();
    }

    private static int getFrameSize(int samplingRate) {
        int size;
        for (int i = 8; i <= 15; i++) {
            size = (int) Math.pow(2, i);
            if (size / (samplingRate * 1.0) >= FFT_FRAME_DURATION) return size;
        }
        throw new IllegalArgumentException("Sampling Frequency of the audio file is too high. Please use a file with a lower Sampling Frequency.");
    }


    /**
     * Method to precalculate the indices to be used to locate the DTMF frequencies in the power spectrum
     */
    public static int[] getCentreIndices(int sampleRate, int frameSize) {
        int[] freqIndices = new int[DTMF_FREQUENCIES_BIN.length];
        for (int i = 0; i < freqIndices.length; i++) {
            int ind = (int) Math.round(((DTMF_FREQUENCIES_BIN[i] * 1.0) / sampleRate) * frameSize);
            freqIndices[i] = ind;
        }
        return freqIndices;
    }

    /**
     * Method to filter out the power spectrum information for the DTMF frequencies given an array of power spectrum information from an FFT.
     *
     * @param frame Frame with power spectrum information to be processed
     * @return an array with 8 doubles. Each representing the magnitude of the corresponding dtmf frequency
     */
    private static double[] filterFrame(int[] freqIndices, double[] frame) {
        double[] out = new double[8];
        for (int i = 0; i < 8; i++) {
            out[i] = frame[freqIndices[i * 3]];
            if (freqIndices[i * 3] != freqIndices[i * 3 + 1]) {
                out[i] += frame[freqIndices[i * 3 + 1]];
            }
            if (freqIndices[i * 3] != freqIndices[i * 3 + 2] && freqIndices[i * 3 + 1] != freqIndices[i * 3 + 2]) {
                out[i] += frame[freqIndices[i * 3 + 2]];
            }
        }
        return out;
    }

    /**
     * Method to return the index of the max element in an array
     *
     * @param arr Array to be processed
     * @return Index of the max element
     */
    public static int maxIndex(double[] arr) {
        int maxAt = 0;
        for (int i = 0; i < arr.length; i++) {
            maxAt = arr[i] > arr[maxAt] ? i : maxAt;
        }
        return maxAt;
    }

    /**
     * Method to decode a frame given the frequency spectrum information of the frame
     *
     * @param dftData Frequency spectrum information showing the relative magnitudes of the power of each DTMF frequency
     * @return DTMF charatcter represented by the frame
     */
    private static char getRawChar(double[] dftData) {
        int lowFreqIndex = maxIndex(Arrays.copyOfRange(dftData, 0, 4));
        int highFreqIndex = maxIndex(Arrays.copyOfRange(dftData, 4, 8));
        return DTMF_KEYPAD_MAPPING[lowFreqIndex][highFreqIndex];
    }

    private static boolean isNoisy(double[] dftData, double[] powerSpectrum) {
        if (powerSpectrum == null) return true;
        double[] temp1 = Arrays.copyOfRange(dftData, 0, 4);
        double[] temp2 = Arrays.copyOfRange(dftData, 4, 8);
        Arrays.sort(temp1);
        Arrays.sort(temp2);
        return ((temp1[temp1.length - 1] + temp2[temp2.length - 1]) / Arrays.stream(powerSpectrum).sum()) < FFT_CUT_OFF_POWER_NOISE_RATIO;
    }

    private static Optional<Character> decodeDtmfFrame(double[] frame, int frameSize, int[] freqIndices) {
        try {
            // slice off the extra bit to make the frameSize a power of 2
            int nextPowerOfTwo = Integer.highestOneBit(frameSize - 1) << 1;
            frame = frame.length < nextPowerOfTwo ? Arrays.copyOf(frame, nextPowerOfTwo) : Arrays.copyOfRange(frame, 0, nextPowerOfTwo);

            // check if the power of the signal is high enough to be accepted.
            if (Arrays.stream(frame).map(Math::abs).average().orElse(0.0) < CUT_OFF_POWER) return Optional.empty();

            // transform frame and return frequency spectrum information
            double[] powerSpectrum = transformFrameFFT(frame);

            // filter out the 8 DTMF frequencies from the power spectrum
            double[] dftData = filterFrame(freqIndices, powerSpectrum);

            // check if the frame has too much noise
            if (isNoisy(dftData, powerSpectrum)) return Optional.empty();

            return Optional.of(getRawChar(dftData));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
