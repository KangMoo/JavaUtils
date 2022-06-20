package com.github.kangmoo.utils.codec.pcm;

public class LpcmResampler {
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

    public static byte[] resample16kTo8k(byte[] data) {
        byte[] resampled = new byte[data.length / 2];
        for (int i = 0; i < resampled.length; i++) {
            resampled[i] = i % 2 == 0 ? data[i * 2] : data[i * 2 + 1];
        }
        return resampled;
    }
}
