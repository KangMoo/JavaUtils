package com.github.kangmoo.utils.audio.pcm;

import javax.sound.sampled.AudioFormat;

/**
 * @author kangmoo Heo
 */
public class AudioFormatBuilder {
    private AudioFormat.Encoding encoding;
    private float sampleRate;
    private int sampleSizeInBits;
    private int channels;
    private int frameSize;
    private float frameRate;
    private boolean bigEndian;

    public AudioFormatBuilder(AudioFormat originalFormat) {
        this.encoding = originalFormat.getEncoding();
        this.sampleRate = originalFormat.getSampleRate();
        this.sampleSizeInBits = originalFormat.getSampleSizeInBits();
        this.channels = originalFormat.getChannels();
        this.frameSize = originalFormat.getFrameSize();
        this.frameRate = originalFormat.getFrameRate();
        this.bigEndian = originalFormat.isBigEndian();
    }

    public AudioFormatBuilder setEncoding(AudioFormat.Encoding encoding) {
        this.encoding = encoding;
        return this;
    }

    public AudioFormatBuilder setSampleRate(float sampleRate) {
        this.sampleRate = sampleRate;
        return this;
    }

    public AudioFormatBuilder setSampleSizeInBits(int sampleSizeInBits) {
        this.sampleSizeInBits = sampleSizeInBits;
        return this;
    }

    public AudioFormatBuilder setChannels(int channels) {
        this.channels = channels;
        return this;
    }

    public AudioFormatBuilder setFrameSize(int frameSize) {
        this.frameSize = frameSize;
        return this;
    }

    public AudioFormatBuilder setFrameRate(float frameRate) {
        this.frameRate = frameRate;
        return this;
    }

    public AudioFormatBuilder setBigEndian(boolean bigEndian) {
        this.bigEndian = bigEndian;
        return this;
    }

    public AudioFormat build() {
        return new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian);
    }
}