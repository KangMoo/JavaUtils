package com.github.kangmoo.utils.audio.pcm;

/**
 * Mix PCM Data
 *
 * @author kangmoo Heo
 */
public class AudioMixer {

    private AudioMixer() {
    }

    public static byte[] mixAudio(byte[] audio1, byte[] audio2, int bitDepth) {
        if (bitDepth != 8 && bitDepth != 16 && bitDepth != 24 && bitDepth != 32) {
            throw new IllegalArgumentException("Unsupported bit depth: " + bitDepth);
        }

        int bytesPerSample = bitDepth / 8;
        int maxLength = Math.max(audio1.length, audio2.length);
        byte[] mixedAudio = new byte[maxLength];

        for (int i = 0; i < maxLength; i += bytesPerSample) {
            int sample1 = getSample(audio1, i, bitDepth);
            int sample2 = getSample(audio2, i, bitDepth);

            int mixedSample = mixSamples(sample1, sample2, bitDepth);

            // 결과를 바이트 배열로 변환
            for (int j = 0; j < bytesPerSample; j++) {
                mixedAudio[i + j] = (byte) ((mixedSample >> (8 * j)) & 0xff);
            }
        }

        return mixedAudio;
    }

    private static int getSample(byte[] audio, int index, int bitDepth) {
        if (index + (bitDepth / 8) - 1 >= audio.length) {
            return 0;
        }

        int sample = 0;
        for (int i = 0; i < (bitDepth / 8); i++) {
            sample |= (audio[index + i] & 0xff) << (8 * i);
        }

        if (bitDepth == 16) {
            sample = (short) sample; // 16-bit sign extension
        } else if (bitDepth == 24) {
            sample = (sample << 8) >> 8; // 24-bit sign extension
        }

        return sample;
    }

    private static int mixSamples(int sample1, int sample2, int bitDepth) {
        // 두 샘플을 평균 내어 믹싱
        int mixedSample = (sample1 + sample2) / 2;

        // 클리핑 처리
        if (bitDepth == 16) {
            mixedSample = Math.max(Short.MIN_VALUE, Math.min(mixedSample, Short.MAX_VALUE));
        } else if (bitDepth == 24 || bitDepth == 32) {
            mixedSample = Math.max(Integer.MIN_VALUE, Math.min(mixedSample, Integer.MAX_VALUE));
        }

        return mixedSample;
    }
}