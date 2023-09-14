package com.github.kangmoo.utils.audio.pcm;

/**
 * @author kangmoo Heo
 */
public class Pcm8k16kResampler {
    /**
     * Resample from 8k to 16k
     *
     * @param data The input data
     * @return The resampled data
     */
    public static byte[] resample8kTo16k(byte[] data) {
        byte[] resampled = new byte[data.length * 2];
        for (int i = 0; i < data.length; i += 2) {
            resampled[i * 2] = data[i];
            resampled[i * 2 + 1] = data[i + 1];
            resampled[i * 2 + 2] = data[i];
            resampled[i * 2 + 3] = data[i + 1];
        }
        return resampled;
    }

    /**
     * Resample from 16k to 8k
     *
     * @param data The input data
     * @return The resampled data
     */
    public static byte[] resample16kTo8k(byte[] data) {
        byte[] resampled = new byte[data.length / 2];
        for (int i = 0; i < resampled.length; i++) {
            resampled[i] = i % 2 == 0 ? data[i * 2] : data[i * 2 + 1];
        }
        return resampled;
    }
}
