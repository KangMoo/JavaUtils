package com.github.kangmoo.utils.audio.pcm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.nio.charset.StandardCharsets;

/**
 * @author kangmoo Heo
 */

@Getter
@Setter
@ToString
public class WavHeader {
    private String chunkId;         // 4 bytes
    private int chunkSize;          // 4 bytes
    private String format;          // 4 bytes
    private String subchunk1Id;     // 4 bytes
    private int subchunk1Size;      // 4 bytes
    private short audioFormat;      // 2 bytes
    private short numChannels;      // 2 bytes
    private int sampleRate;         // 4 bytes
    private int byteRate;           // 4 bytes
    private short blockAlign;       // 2 bytes
    private short bitsPerSample;    // 2 bytes
    private String subchunk2Id;     // 4 bytes
    private int subchunk2Size;      // 4 bytes

    // 44 bytes 생성자
    public WavHeader(byte[] header) {
        if (header.length != 44) {
            throw new IllegalArgumentException("Header must be 44 bytes");
        }

        this.chunkId = new String(readBytes(header, 0, 4), StandardCharsets.US_ASCII);
        this.chunkSize = readInt(header, 4);
        this.format = new String(readBytes(header, 8, 4), StandardCharsets.US_ASCII);
        this.subchunk1Id = new String(readBytes(header, 12, 4), StandardCharsets.US_ASCII);
        this.subchunk1Size = readInt(header, 16);
        this.audioFormat = readShort(header, 20);
        this.numChannels = readShort(header, 22);
        this.sampleRate = readInt(header, 24);
        this.byteRate = readInt(header, 28);
        this.blockAlign = readShort(header, 32);
        this.bitsPerSample = readShort(header, 34);
        this.subchunk2Id = new String(readBytes(header, 36, 4), StandardCharsets.US_ASCII);
        this.subchunk2Size = readInt(header, 40);
    }

    public WavHeader(int sampleRate, int bitsPerSampleInt, int numChannelsInt, int bodySize) {
        if (bitsPerSampleInt < Short.MIN_VALUE || bitsPerSampleInt > Short.MAX_VALUE) {
            throw new IllegalArgumentException("Bits per sample value out of range for a short data type");
        }

        if (numChannelsInt < Short.MIN_VALUE || numChannelsInt > Short.MAX_VALUE) {
            throw new IllegalArgumentException("Number of channels value out of range for a short data type");
        }
        this.chunkId = "RIFF";
        this.format = "WAVE";
        this.subchunk1Id = "fmt ";
        this.subchunk1Size = 16; // PCM Audio의 fmt chunk는 항상 16
        this.audioFormat = 1;   // PCM
        this.subchunk2Id = "data";

        this.sampleRate = sampleRate;
        this.bitsPerSample = (short) bitsPerSampleInt;  // convert int to short
        this.numChannels = (short) numChannelsInt;  // convert int to short
        this.subchunk2Size = bodySize;

        updateByteRate(); // byteRate와 blockAlign 업데이트
        this.blockAlign = (short) (numChannels * bitsPerSample / 8);
        this.chunkSize = 36 + bodySize;
    }

    public byte[] toBytes() {
        byte[] header = new byte[44];

        System.arraycopy(chunkId.getBytes(StandardCharsets.US_ASCII), 0, header, 0, 4);
        writeInt(chunkSize, header, 4);
        System.arraycopy(format.getBytes(StandardCharsets.US_ASCII), 0, header, 8, 4);
        System.arraycopy(subchunk1Id.getBytes(StandardCharsets.US_ASCII), 0, header, 12, 4);
        writeInt(subchunk1Size, header, 16);
        writeShort(audioFormat, header, 20);
        writeShort(numChannels, header, 22);
        writeInt(sampleRate, header, 24);
        writeInt(byteRate, header, 28);
        writeShort(blockAlign, header, 32);
        writeShort(bitsPerSample, header, 34);
        System.arraycopy(subchunk2Id.getBytes(StandardCharsets.US_ASCII), 0, header, 36, 4);
        writeInt(subchunk2Size, header, 40);

        return header;
    }

    private byte[] readBytes(byte[] data, int start, int length) {
        byte[] result = new byte[length];
        System.arraycopy(data, start, result, 0, length);
        return result;
    }

    private int readInt(byte[] data, int start) {
        return (data[start] & 0xFF) |
                ((data[start + 1] & 0xFF) << 8) |
                ((data[start + 2] & 0xFF) << 16) |
                ((data[start + 3] & 0xFF) << 24);
    }

    private short readShort(byte[] data, int start) {
        return (short) ((data[start] & 0xFF) | ((data[start + 1] & 0xFF) << 8));
    }

    private void writeInt(int value, byte[] data, int start) {
        data[start] = (byte) (value & 0xFF);
        data[start + 1] = (byte) ((value >> 8) & 0xFF);
        data[start + 2] = (byte) ((value >> 16) & 0xFF);
        data[start + 3] = (byte) ((value >> 24) & 0xFF);
    }

    private void writeShort(short value, byte[] data, int start) {
        data[start] = (byte) (value & 0xFF);
        data[start + 1] = (byte) ((value >> 8) & 0xFF);
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
        updateByteRate();
    }

    public void setNumChannels(short numChannels) {
        this.numChannels = numChannels;
        updateByteRate();
    }

    public void setBitsPerSample(short bitsPerSample) {
        this.bitsPerSample = bitsPerSample;
        updateByteRate();
    }

    private void updateByteRate() {
        this.byteRate = this.sampleRate * this.numChannels * this.bitsPerSample / 8;
    }

}
