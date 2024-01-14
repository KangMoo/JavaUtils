package com.github.kangmoo.utils.audio.pcm;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import static javax.sound.sampled.AudioFormat.Encoding;

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

    public static byte[] getWavHeaderByte(float sampleRate, int sampleSizeInBits, int channels, boolean signed, boolean bigEndian, int audioDataLength){
        return getWavHeaderByte(new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian), audioDataLength);
    }

    public static byte[] getWavHeaderByte(Encoding encoding, float sampleRate, int sampleSizeInBits, int channels, int frameSize, float frameRate, boolean bigEndian, int audioDataLength) {
        return getWavHeaderByte(new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian), audioDataLength);
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

    public static byte[] convert16bitTo8bit(byte[] pcm16bit) {
        if (pcm16bit == null) {
            throw new NullPointerException("pcm16bit is null");
        }

        if (pcm16bit.length <= 1) {
            return new byte[0];
        } else if (pcm16bit.length % 2 != 0) {
            pcm16bit = Arrays.copyOf(pcm16bit, pcm16bit.length - 1);
        }

        byte[] pcm8bit = new byte[pcm16bit.length / 2];

        for (int i = 0; i < pcm8bit.length; i++) {
            int high = pcm16bit[2 * i + 1];
            int low = pcm16bit[2 * i];
            int sample16bit = (high << 8) | (low & 0xFF);

            // 16비트 샘플 값을 -32768 ~ 32767 범위에서 0 ~ 255 범위로 스케일링
            int sample8bit = (sample16bit + 32768) / 256;

            pcm8bit[i] = (byte) sample8bit;
        }

        return pcm8bit;
    }
}
