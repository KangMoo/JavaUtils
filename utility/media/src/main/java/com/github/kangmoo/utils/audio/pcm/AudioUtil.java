package com.github.kangmoo.utils.audio.pcm;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static javax.sound.sampled.AudioFormat.Encoding;

/**
 * @author kangmoo Heo
 */
public class AudioUtil {
    private AudioUtil() {
    }

    public static byte[] getWavHeaderByte(Encoding encoding, float sampleRate, int sampleSizeInBits, int channels, int frameSize, float frameRate, boolean bigEndian, int audioDataLength) {
        return getWavHeaderByte(new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian), audioDataLength);
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
        buffer.putShort((short) (format.getEncoding() == Encoding.PCM_SIGNED ? 1 : 3)); // AudioFormat
        buffer.putShort((short) format.getChannels());
        buffer.putInt((int) format.getSampleRate());
        buffer.putInt(byteRate);
        buffer.putShort((short) blockAlign);
        buffer.putShort((short) format.getSampleSizeInBits());
        buffer.put("data".getBytes());
        buffer.putInt(audioDataLength); // Subchunk2Size

        return buffer.array();
    }

    public static void resample(File audioFile, File outputFile, int sampleRate) throws UnsupportedAudioFileException, IOException {
        AudioInputStream stream = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat format = stream.getFormat();
        AudioFormat newFormat = new AudioFormat(format.getEncoding(), sampleRate, format.getSampleSizeInBits(), format.getChannels(), format.getFrameSize(), sampleRate, format.isBigEndian());
        AudioInputStream newStream = AudioSystem.getAudioInputStream(newFormat, stream);
        AudioSystem.write(newStream, AudioFileFormat.Type.WAVE, outputFile);
    }

    public static void joinAudioFile(File wavFile1, File wavFile2, File mixedFile) throws UnsupportedAudioFileException, IOException {
        AudioInputStream stream1 = AudioSystem.getAudioInputStream(wavFile1);
        AudioInputStream stream2 = AudioSystem.getAudioInputStream(wavFile2);

        AudioInputStream mixedStream = new AudioInputStream(
                new SequenceInputStream(stream1, stream2),
                stream1.getFormat(),
                stream1.getFrameLength() + stream2.getFrameLength());

        AudioSystem.write(mixedStream, AudioFileFormat.Type.WAVE, mixedFile); // 혼합된 파일 저장
    }

    public static void mixAudioFile(File wavFile1, File wavFile2, File mixedFile) throws UnsupportedAudioFileException, IOException {
        AudioInputStream stream1 = AudioSystem.getAudioInputStream(wavFile1);
        AudioInputStream stream2 = AudioSystem.getAudioInputStream(wavFile2);

        AudioFormat format = stream1.getFormat();
        if (!format.equals(stream2.getFormat())) {
            throw new IllegalArgumentException("Audio Formats must be same");
        }

        byte[] buffer1 = new byte[1024];
        byte[] buffer2 = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        while (true) {
            int read1 = stream1.read(buffer1, 0, buffer1.length);
            int read2 = stream2.read(buffer2, 0, buffer2.length);

            if (read1 == -1 || read2 == -1) {
                break;
            }

            for (int i = 0; i < Math.min(read1, read2); i++) {
                buffer1[i] = (byte) ((buffer1[i] + buffer2[i]) / 2);
            }

            outputStream.write(buffer1, 0, Math.min(read1, read2));
        }

        byte[] mixedBytes = outputStream.toByteArray();
        ByteArrayInputStream mixedInput = new ByteArrayInputStream(mixedBytes);
        AudioInputStream mixedStream = new AudioInputStream(mixedInput, format, mixedBytes.length / format.getFrameSize());

        AudioSystem.write(mixedStream, AudioFileFormat.Type.WAVE, mixedFile);
    }

    private static class MixingInputStream extends InputStream {
        private final AudioInputStream stream1;
        private final AudioInputStream stream2;

        public MixingInputStream(AudioInputStream stream1, AudioInputStream stream2) {
            this.stream1 = stream1;
            this.stream2 = stream2;
        }

        @Override
        public int read() throws IOException {
            int byte1 = stream1.read();
            int byte2 = stream2.read();

            if (byte1 == -1 || byte2 == -1) return -1;

            return (byte1 + byte2) / 2;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            byte[] tempBuffer1 = new byte[len];
            byte[] tempBuffer2 = new byte[len];

            int read1 = stream1.read(tempBuffer1, 0, len);
            int read2 = stream2.read(tempBuffer2, 0, len);

            if (read1 == -1 || read2 == -1) return -1;

            for (int i = 0; i < len; i++) {
                int byte1 = tempBuffer1[i];
                int byte2 = tempBuffer2[i];
                b[i] = (byte) ((byte1 + byte2) / 2);
            }

            return len;
        }
    }
}
