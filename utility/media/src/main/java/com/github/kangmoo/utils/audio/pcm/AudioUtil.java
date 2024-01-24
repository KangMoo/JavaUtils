package com.github.kangmoo.utils.audio.pcm;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author kangmoo Heo
 */
public class AudioUtil {
    private AudioUtil() {
    }

    public static byte[] getWavHeaderByte(AudioFormat format, int audioDataLength) {
        int byteRate = (int) format.getSampleRate() * format.getChannels() * format.getSampleSizeInBits() / 8;
        int blockAlign = format.getChannels() * format.getSampleSizeInBits() / 8;

        ByteBuffer buffer = ByteBuffer.allocate(44);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.put("RIFF".getBytes());
        buffer.putInt(audioDataLength + 36); // ChunkSize
        buffer.put("WAVE".getBytes());
        buffer.put("fmt ".getBytes());
        buffer.putInt(16); // Subchunk1Size for PCM
        buffer.putShort((short) (format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED ? 1 : 3)); // AudioFormat
        buffer.putShort((short) format.getChannels());
        buffer.putInt((int) format.getSampleRate());
        buffer.putInt(byteRate);
        buffer.putShort((short) blockAlign);
        buffer.putShort((short) format.getSampleSizeInBits());
        buffer.put("data".getBytes());
        buffer.putInt(audioDataLength); // Subchunk2Size

        return buffer.array();
    }

    public static byte[] getWavHeaderByte(float sampleRate, int sampleSizeInBits, int channels, boolean signed, boolean bigEndian, int audioDataLength) {
        return getWavHeaderByte(new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian), audioDataLength);
    }

    public static byte[] getWavHeaderByte(AudioFormat.Encoding encoding, float sampleRate, int sampleSizeInBits, int channels, int frameSize, float frameRate, boolean bigEndian, int audioDataLength) {
        return getWavHeaderByte(new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian), audioDataLength);
    }

    public static AudioFormat getAudioFormatFromWavHeader(byte[] wavHeader) {
        ByteBuffer buffer = ByteBuffer.wrap(wavHeader);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.position(20);
        short audioFormat = buffer.getShort();
        short channels = buffer.getShort();
        int sampleRate = buffer.getInt();
        buffer.getInt(); // byte rate
        short blockAlign = buffer.getShort();
        short sampleSizeInBits = buffer.getShort();

        boolean bigEndian = false;
        AudioFormat.Encoding encoding = (audioFormat == 1) ? AudioFormat.Encoding.PCM_SIGNED : AudioFormat.Encoding.PCM_UNSIGNED;

        return new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, blockAlign, sampleRate, bigEndian);
    }

    public static byte[] convertFormat(byte[] bytes, AudioFormat from, AudioFormat to) throws IOException {
        if (from.equals(to)) return bytes;
        try (AudioInputStream fromStream = new AudioInputStream(new ByteArrayInputStream(bytes), from, bytes.length); AudioInputStream toStream = AudioSystem.getAudioInputStream(to, fromStream)) {
            return toStream.readAllBytes();
        }
    }

    public static byte[] mixAudio(byte[] bytes1, byte[] bytes2, int bitDepth) {
        ByteBuffer buffer1 = ByteBuffer.wrap(bytes1).order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer buffer2 = ByteBuffer.wrap(bytes2).order(ByteOrder.LITTLE_ENDIAN);
        int bufferLength = Math.max(buffer1.remaining(), buffer2.remaining());
        ByteBuffer resultBuffer = ByteBuffer.allocate(bufferLength).order(ByteOrder.LITTLE_ENDIAN);

        int bytesPerSample = bitDepth / 8;
        for (int i = 0; i < bufferLength; i += bytesPerSample) {
            try {
                long sample1 = buffer1.remaining() > i + bytesPerSample ? getSample(buffer1, i, bytesPerSample) : 0;
                long sample2 = buffer2.remaining() > i + bytesPerSample ? getSample(buffer2, i, bytesPerSample) : 0;
                long mixedSample = mixSamples(sample1, sample2, bytesPerSample);
                putSample(resultBuffer, i, mixedSample, bytesPerSample);
            } catch (Exception e) {
                break;
            }
        }

        return resultBuffer.array();
    }

    public static AudioInputStream mixAudio(AudioInputStream stream1, AudioInputStream stream2, AudioFormat audioFormat) throws IOException {
        stream1 = AudioSystem.getAudioInputStream(audioFormat, stream1);
        stream2 = AudioSystem.getAudioInputStream(audioFormat, stream2);

        byte[] bytes1 = stream1.readAllBytes();
        byte[] bytes2 = stream2.readAllBytes();

        int bitDepth = audioFormat.getSampleSizeInBits();
        ByteBuffer buffer1 = ByteBuffer.wrap(bytes1).order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer buffer2 = ByteBuffer.wrap(bytes2).order(ByteOrder.LITTLE_ENDIAN);
        int bufferLength = Math.max(buffer1.remaining(), buffer2.remaining());
        ByteBuffer resultBuffer = ByteBuffer.allocate(bufferLength).order(ByteOrder.LITTLE_ENDIAN);

        int bytesPerSample = bitDepth / 8;
        for (int i = 0; i < bufferLength; i += bytesPerSample) {
            try {
                long sample1 = buffer1.remaining() > i + bytesPerSample ? getSample(buffer1, i, bytesPerSample) : 0;
                long sample2 = buffer2.remaining() > i + bytesPerSample ? getSample(buffer2, i, bytesPerSample) : 0;
                long mixedSample = mixSamples(sample1, sample2, bytesPerSample);
                putSample(resultBuffer, i, mixedSample, bytesPerSample);
            } catch (Exception e) {
                break;
            }
        }

        byte[] mixedBytes = resultBuffer.array();

        ByteArrayInputStream mixedInput = new ByteArrayInputStream(mixedBytes);
        return new AudioInputStream(mixedInput, audioFormat, mixedBytes.length);
    }

    private static long getSample(ByteBuffer buffer, int index, int byteSize) {
        if (byteSize > 8) throw new IllegalArgumentException("byteSize must be less than 8");
        long sample = 0;
        for (int i = 0; i < byteSize; i++) {
            sample |= (buffer.get(index + i) & 0xFFL) << (8 * i);
        }

        if ((sample & (1L << (byteSize * 8 - 1))) != 0) {
            for (int i = byteSize * 8; i < 64; i++) {
                sample |= 1L << i;
            }
        }
        return sample;
    }

    private static void putSample(ByteBuffer buffer, int index, long value, int byteSize) {
        if (byteSize > 8) throw new IllegalArgumentException("byteSize must be less than 8");
        for (int i = 0; i < byteSize; i++) {
            buffer.put(index + i, (byte) ((value >> (8 * i)) & 0xFF));
        }
    }

    private static long mixSamples(long sample1, long sample2, int byteSize) {
        long mixedSample = (sample1 / 2) + (sample2 / 2);
        long maxSampleValue = (1L << (byteSize * 8 - 1)) - 1;
        long minSampleValue = -(1L << (byteSize * 8 - 1));
        if (mixedSample > maxSampleValue) {
            return maxSampleValue;
        } else if (mixedSample < minSampleValue) {
            return minSampleValue;
        } else {
            return mixedSample;
        }
    }
}
