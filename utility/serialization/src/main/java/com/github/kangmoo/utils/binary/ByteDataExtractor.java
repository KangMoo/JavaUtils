package com.github.kangmoo.utils.binary;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @author kangmoo Heo
 */
public class ByteDataExtractor {
    private ByteDataExtractor() {
    }

    public static char getAsChar(byte[] bytes, int offset, int size) {
        if (size < 0 || size > 2) throw new IllegalArgumentException("size is must be in range 0 ~ 2");
        return ByteBuffer.wrap(getSubBytes(bytes, offset, size, 2)).getChar();
    }

    public static short getAsShort(byte[] bytes, int offset, int size) {
        if (size < 0 || size > 2) throw new IllegalArgumentException("size is must be in range 0 ~ 2");
        return ByteBuffer.wrap(getSubBytes(bytes, offset, size, 2)).getShort();
    }

    public static int getAsInt(byte[] bytes, int offset, int size) {
        if (size < 0 || size > 4) throw new IllegalArgumentException("size is must be in range 0 ~ 4");
        return ByteBuffer.wrap(getSubBytes(bytes, offset, size, 4)).getInt();
    }

    public static long getAsLong(byte[] bytes, int offset, int size) {
        if (size < 0 || size > 8) throw new IllegalArgumentException("size is must be in range 0 ~ 8");
        return ByteBuffer.wrap(getSubBytes(bytes, offset, size, 8)).getLong();
    }

    public static float getAsFloat(byte[] bytes, int offset, int size) {
        if (size < 0 || size > 4) throw new IllegalArgumentException("size is must be in range 0 ~ 4");
        return ByteBuffer.wrap(getSubBytes(bytes, offset, size, 4)).getFloat();
    }

    public static double getAsDouble(byte[] bytes, int offset, int size) {
        if (size < 0 || size > 8) throw new IllegalArgumentException("size is must be in range 0 ~ 8");
        return ByteBuffer.wrap(getSubBytes(bytes, offset, size, 8)).getDouble();
    }

    public static String getAsString(byte[] bytes, int offset, int size) {
        return new String(Arrays.copyOfRange(bytes, offset, size));
    }

    private static byte[] getSubBytes(byte[] bytes, int offset, int size, int dataSize) {
        byte[] data = new byte[dataSize];
        System.arraycopy(bytes, offset, data, 0, Math.min(size, dataSize));
        return data;
    }
}
