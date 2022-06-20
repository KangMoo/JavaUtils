package com.github.kangmoo.utils.codec.pcm.audioresampler.audio;

import java.nio.ByteOrder;

/**
 * Encapsulates a set of properties used to analyze
 * and process audio data.
 *
 * @author Raphaël Zumer <rzumer@gmail.com>
 */
public class AudioProperties {
    public short NumChannels;
	public int SampleRate;
    public short BitsPerSample;
    public int DataSize;
    public ByteOrder ByteOrder;
    public AudioFormat Format;

    public AudioProperties() {
    }

    public AudioProperties(short numChannels, int sampleRate, short bitsPerSample, int dataSize, java.nio.ByteOrder byteOrder, AudioFormat format) {
        NumChannels = numChannels;
        SampleRate = sampleRate;
        BitsPerSample = bitsPerSample;
        DataSize = dataSize;
        ByteOrder = byteOrder;
        Format = format;
    }

    public int getChannelSize() {
        return (BitsPerSample / 8);
    }

    public int getFrameSize() {
        return getChannelSize() * NumChannels;
    }

    public AudioProperties copy() {
        AudioProperties copy = new AudioProperties();
        copy.NumChannels = NumChannels;
        copy.SampleRate = SampleRate;
        copy.BitsPerSample = BitsPerSample;
        copy.DataSize = DataSize;
        copy.ByteOrder = ByteOrder;
        copy.Format = Format;

        return copy;
    }

    public enum AudioFormat {
        NONE,
        WAVE_PCM
    }
}
