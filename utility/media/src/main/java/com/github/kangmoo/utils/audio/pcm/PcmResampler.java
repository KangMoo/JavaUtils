package com.github.kangmoo.utils.audio.pcm;

import java.util.Arrays;
import java.util.Optional;

public class PcmResampler {

    private final int inSampleRate;
    private final int outSampleRate;
    private final short numChannels;
    private final short bitsPerSample;
    private final int frameSize;
    private final int padding;
    private byte[] lastFrameProcessed;
    private int decimationOffset;

    public PcmResampler(int inSampleRate, int outSampleRate) {
        this(inSampleRate, outSampleRate, (short) 1, (short) 16);
    }

    private PcmResampler(int inSampleRate, int outSampleRate, short numChannels, short bitsPerSample) {
        if (bitsPerSample <= 0 || bitsPerSample > 16) {
            throw new IllegalArgumentException("The value of bitsPerSample is incorrect.");
        }
        if (inSampleRate <= 0 || outSampleRate <= 0) {
            throw new IllegalArgumentException("Sampling rate must be positive integer");
        }
        this.inSampleRate = inSampleRate;
        this.outSampleRate = outSampleRate;
        this.numChannels = numChannels;
        this.bitsPerSample = bitsPerSample;
        this.frameSize = (this.bitsPerSample / 8) * this.numChannels;
        this.padding = ((leastCommonMultiple(inSampleRate, outSampleRate) / inSampleRate) - 1) * frameSize;
    }

    public byte[] process(byte[] input) {
        if (noProcessingRequired(input)) {
            return input;
        }

        byte[] stuffedInput = addPaddingToInput(input);
        performLinearInterpolation(stuffedInput);
        return performDecimation(stuffedInput);
    }

    private boolean noProcessingRequired(byte[] input) {
        return outSampleRate == inSampleRate || input.length == 0;
    }

    private byte[] addPaddingToInput(byte[] input) {
        int baseLength = input.length + (input.length / frameSize * padding);
        byte[] stuffedInput = new byte[lastFrameProcessed == null ? baseLength - padding : baseLength];

        int stuffedInputPointer = lastFrameProcessed == null ? 0 : padding;

        // 입력 데이터에 패딩 추가
        for (int i = 0; i < input.length; i += frameSize) {
            byte[] frame = Arrays.copyOfRange(input, i, i + frameSize);

            for (byte b : frame) {
                stuffedInput[stuffedInputPointer] = b;
                stuffedInputPointer++;
            }

            stuffedInputPointer += padding;
        }

        return stuffedInput;
    }

    private void performLinearInterpolation(byte[] stuffedInput) {
        for (int i = -(lastFrameProcessed == null ? 0 : frameSize); i < stuffedInput.length; i += frameSize + padding) {
            processFrameForInterpolation(stuffedInput, i, frameSize, padding);
        }
    }

    private void processFrameForInterpolation(byte[] stuffedInput, int i, int frameSize, int padding) {
        int j = i + frameSize + padding;

        byte[] leftFrame = extractFrame(stuffedInput, i, frameSize).orElse(null);
        byte[] rightFrame = extractFrame(stuffedInput, j, frameSize).orElse(null);

        for (int channel = 0; channel < this.numChannels; channel++) {
            int[] samples = extractSamples(leftFrame, rightFrame, channel);

            interpolateSamples(stuffedInput, i, samples, channel, frameSize, padding);
        }
    }

    private Optional<byte[]> extractFrame(byte[] stuffedInput, int index, int frameSize) {
        if (index >= 0 && index + frameSize <= stuffedInput.length) {
            return Optional.of(Arrays.copyOfRange(stuffedInput, index, index + frameSize));
        } else if (index < 0) {
            return Optional.of(lastFrameProcessed);
        }
        return Optional.empty();
    }

    private int[] extractSamples(byte[] leftFrame, byte[] rightFrame, int channel) {
        int leftSample = (leftFrame != null) ? extractSample(leftFrame, channel) : 0;
        int rightSample = (rightFrame != null) ? extractSample(rightFrame, channel) : 0;

        return new int[]{leftSample, rightSample};
    }

    private int extractSample(byte[] frame, int channel) {
        if (this.bitsPerSample > 8) {
            return getIntFromBytes(Arrays.copyOfRange(frame, channel * (this.bitsPerSample / 8), (channel + 1) * (this.bitsPerSample / 8)));
        }
        return getUnsignedIntFromByte(frame[channel]);
    }

    private void interpolateSamples(byte[] stuffedInput, int i, int[] samples, int channel, int frameSize, int padding) {
        for (int k = 0; k < (padding / frameSize); k++) {
            int index = i + (frameSize * (k + 1)) + (channel * (this.bitsPerSample / 8));
            if (index + (this.bitsPerSample / 8) <= stuffedInput.length) {
                byte[] interpolatedBytes = getInterpolatedBytes(samples, k, padding, frameSize);
                System.arraycopy(interpolatedBytes, 0, stuffedInput, index, interpolatedBytes.length);
            }
        }
    }

    private byte[] getInterpolatedBytes(int[] samples, int k, int padding, int frameSize) {
        int interpolatedSample = interpolateLinear(samples[0], samples[1], (k + 1) / (((double) padding / frameSize) + 1));
        if (this.bitsPerSample == 8) {
            return new byte[]{(byte) interpolatedSample};
        } else if (this.bitsPerSample == 16) {
            return getShortBytes((short) interpolatedSample);
        }
        return getIntBytes(interpolatedSample);
    }


    private byte[] performDecimation(byte[] stuffedInput) {
        if (inSampleRate <= 0 || outSampleRate <= 0) {
            throw new IllegalArgumentException("Sampling rate must be positive integer");
        }
        int sampleRateLCM = leastCommonMultiple(inSampleRate, outSampleRate);
        int decimationRate = sampleRateLCM / outSampleRate;

        byte[] reSampledInput = new byte[stuffedInput.length];

        int outputSize = (int) Math.round(((double) stuffedInput.length / decimationRate));

        // 출력 파일의 끝이 일정하지 않으면 추가 바이트 삽입
        while (outputSize % frameSize != 0) {
            outputSize++;
        }
        int decimatedInputPointer = 0;

        // 샘플 감소를 통한 재샘플링
        for (int i = decimationOffset * frameSize; i < stuffedInput.length; i += decimationRate * frameSize) {
            byte[] frame = Arrays.copyOfRange(stuffedInput, i, i + frameSize);

            for (byte b : frame) {
                reSampledInput[decimatedInputPointer] = b;
                decimatedInputPointer++;
            }

            decimationOffset = Math.abs(stuffedInput.length - (decimationRate * frameSize) - i) / frameSize;
        }

        // 남은 바이트에 silence 값 (0) 채우기
        while (decimatedInputPointer < outputSize) {
            reSampledInput[decimatedInputPointer] = (byte) (0);
            decimatedInputPointer++;
        }

        // 처리된 마지막 프레임을 저장하여 다음 호출에 사용
        lastFrameProcessed = Arrays.copyOfRange(stuffedInput, stuffedInput.length - frameSize, stuffedInput.length);

        return reSampledInput;
    }

    private static int leastCommonMultiple(int a, int b) {
        return a / greatestCommonDivisor(a, b) * b;
    }

    private static int greatestCommonDivisor(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private static int interpolateLinear(int a, int b, double distance) {
        if (distance <= 0) {
            return a;
        } else if (distance >= 1) {
            return b;
        }
        return (int) Math.round(a + ((b - a) * distance));
    }

    private static int getIntFromBytes(byte[] data) {
        if (data == null || data.length == 0) {
            return 0;
        }
        switch (data.length) {
            case 1:
                return data[0];
            case 2:
                return (data[1] << 8) | (data[0] & 0xff);
            case 3:
                return (data[2] << 16) | ((data[1] & 0xff) << 8) | (data[0] & 0xff);
            case 4:
                return (data[3] << 24) | ((data[2] & 0xff) << 16) | ((data[1] & 0xff) << 8) | (data[0] & 0xff);
            default:
                throw new IllegalArgumentException();
        }
    }

    private static int getUnsignedIntFromByte(byte data) {
        return data & 0xff;
    }

    private static byte[] getIntBytes(int data) {
        return new byte[]{
                (byte) (data),
                (byte) (data >> 8),
                (byte) (data >> 16),
                (byte) (data >> 24)
        };
    }

    private static byte[] getShortBytes(short data) {
        return new byte[]{
                (byte) (data),
                (byte) (data >> 8)
        };
    }
}
