package com.github.kangmoo.utils.audio.pcm;

/**
 * @author kangmoo Heo
 *
 * PCM 데이터를 이용하여 평균 볼륨 레벨을 계산하는 클래스
 */
public class VolumeCalculator {
    private VolumeCalculator() {
    }

    /**
     * PCM 데이터로부터 평균 볼륨 레벨을 계산한다.
     *
     * @param bytes         PCM 데이터
     * @param bitsPerSample 샘플당 비트 수
     * @return 평균 볼륨 레벨 (0 ~ 100)
     * @throws IllegalArgumentException 인자 값이 null이거나 비트 수가 잘못된 경우 발생
     */
    public static int calculateVolumeLevel(byte[] bytes, int bitsPerSample) throws IllegalArgumentException {
        if (bytes == null) {
            throw new IllegalArgumentException("PCM data is null.");
        }
        if (bitsPerSample % 8 != 0 || bitsPerSample <= 0 || bitsPerSample > 32) {
            throw new IllegalArgumentException("The number of bits is incorrect. Must be one of 8, 16, 24, or 32.");
        }

        long totalSquared = 0;
        int bytesPerSample = bitsPerSample / 8;

        for (int i = 0; i < bytes.length; i += bytesPerSample) {
            int sample = getLittleEndianInt(bytes, i, bytesPerSample);
            totalSquared += (long) sample * sample;
        }

        double rms = Math.sqrt(totalSquared / (bytes.length / (double) bytesPerSample));

        double maxRms = Math.pow(2, bitsPerSample - 1);
        return (int) (rms / maxRms * 100);
    }

    /**
     * 바이트 배열에서 지정된 위치에 있는 리틀 엔디안 형식의 정수를 반환한다.
     *
     * @param bytes          바이트 배열
     * @param offset         위치
     * @param bytesPerSample 샘플당 바이트 수
     * @return 정수
     */
    private static int getLittleEndianInt(byte[] bytes, int offset, int bytesPerSample) {
        int value = 0;
        for (int i = 0; i < bytesPerSample; i++) {
            value |= (bytes[offset + i] & 0xff) << (8 * i);
        }
        if ((bytes[offset + bytesPerSample - 1] & 0x80) != 0) {
            for (int i = bytesPerSample; i < 4; i++) {
                value |= 0xff << (8 * i);
            }
        }
        return value;
    }
}
