package com.github.kangmoo.utils.codec.pcm.audioresampler.audio;

import com.github.kangmoo.utils.codec.pcm.audioresampler.audio.AudioProperties.AudioFormat;

import java.nio.ByteOrder;

/**
 * Represents a filter able to accept input audio data,
 * performs deterministic processing on it, and returns the output.
 *
 * @author Raphaël Zumer <rzumer@gmail.com>
 */
public abstract class AudioFilter {
    AudioProperties properties;
    AudioProperties outProperties;

    public AudioFilter() {
        properties = new AudioProperties((short) 1, 8000, (short) 16, 0, ByteOrder.LITTLE_ENDIAN, AudioFormat.WAVE_PCM);
        outProperties = new AudioProperties((short) 1, 8000, (short) 16, 0, ByteOrder.LITTLE_ENDIAN, AudioFormat.WAVE_PCM);
    }

    /**
     * Filter the input data.
     * The function should make sure the input data is valid beforehand.
     */
    public abstract byte[] process(byte[] input);

    public void setInputProperties(AudioProperties properties) {
        this.properties = properties;
    }

    public AudioProperties getOutputProperties() {
        return outProperties;
    }
}
