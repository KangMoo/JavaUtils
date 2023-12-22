package com.github.kangmoo.utils.audio.pcm;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Mix PCM Data
 *
 * @author kangmoo Heo
 */
public class AudioMixer {

    /**
     * Mix 16bit, mono pcm
     */
    public static byte[] mix16bitMonoLpcm(byte[] is1, byte[] is2) {
        int bufferLength = Math.min(is1.length, is2.length);

        byte[] average = new byte[bufferLength];
        short[] shorts1 = new short[bufferLength / 2];
        ByteBuffer.wrap(is1).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts1);

        short[] shorts2 = new short[bufferLength / 2];
        ByteBuffer.wrap(is2).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts2);

        short[] result = new short[bufferLength / 2];

        for (int i = 0; i < result.length; i++) {
            result[i] = (short) ((shorts1[i] + shorts2[i]) / 2);
        }

        ByteBuffer.wrap(average).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(result);
        return average;
    }

    /**
     * Mix 32bit, mono pcm
     */
    public static byte[] mix32bitMonoLpcm(byte[] is1, byte[] is2) {
        int bufferLength = Math.min(is1.length, is2.length);

        byte[] average = new byte[bufferLength];

        int[] ints1 = new int[bufferLength / 4];
        ByteBuffer.wrap(is1).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer().get(ints1);

        int[] ints2 = new int[bufferLength / 4];
        ByteBuffer.wrap(is2).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer().get(ints2);

        int[] result = new int[bufferLength / 4];

        for (int i = 0; i < result.length; i++) {
            result[i] = ((ints1[i] + ints2[i]) / 2);
        }

        ByteBuffer.wrap(average).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer().put(result);
        return average;
    }

}