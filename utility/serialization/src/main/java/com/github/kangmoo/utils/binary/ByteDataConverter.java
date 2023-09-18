package com.github.kangmoo.utils.binary;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * @author kangmoo Heo
 */
public class ByteDataConverter {
    private ByteDataConverter() {
    }

    public static char getAsChar(byte[] bytes, int offset, int size, ByteOrder byteOrder) {
        return ByteBuffer.wrap(getSubBytes(bytes, offset, size, Character.BYTES, byteOrder)).order(byteOrder).getChar();
    }

    public static short getAsShort(byte[] bytes, int offset, int size, ByteOrder byteOrder) {
        return ByteBuffer.wrap(getSubBytes(bytes, offset, size, Short.BYTES, byteOrder)).order(byteOrder).getShort();
    }

    public static int getAsInt(byte[] bytes, int offset, int size, ByteOrder byteOrder) {
        return ByteBuffer.wrap(getSubBytes(bytes, offset, size, Integer.BYTES, byteOrder)).order(byteOrder).getInt();
    }

    public static long getAsLong(byte[] bytes, int offset, int size, ByteOrder byteOrder) {
        return ByteBuffer.wrap(getSubBytes(bytes, offset, size, Long.BYTES, byteOrder)).order(byteOrder).getLong();
    }

    public static float getAsFloat(byte[] bytes, int offset, int size, ByteOrder byteOrder) {
        return ByteBuffer.wrap(getSubBytes(bytes, offset, size, Float.BYTES, byteOrder)).order(byteOrder).getFloat();
    }

    public static double getAsDouble(byte[] bytes, int offset, int size, ByteOrder byteOrder) {
        return ByteBuffer.wrap(getSubBytes(bytes, offset, size, Double.BYTES, byteOrder)).order(byteOrder).getDouble();
    }

    public static boolean getAsBoolean(byte[] bytes, int offset) {
        return bytes[offset] != 0;
    }

    public static String getAsString(byte[] bytes, int offset, int size) {
        return size == 0 ?
                new String(Arrays.copyOfRange(bytes, offset, bytes.length)) :
                new String(Arrays.copyOfRange(bytes, offset, offset + size));
    }

    private static byte[] getSubBytes(byte[] bytes, int offset, int size, int dataSize, ByteOrder byteOrder) {
        if (size == 0) size = dataSize;
        byte[] data = new byte[dataSize];
        int length = Math.min(size, dataSize);
        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            System.arraycopy(bytes, offset, data, 0, length);
        } else {
            System.arraycopy(bytes, offset, data, dataSize - length, length);
        }
        return data;
    }

    public static byte[] getAsByte(char value, int size, ByteOrder byteOrder) {
        if (size == 0) size = Character.BYTES;
        byte[] array = ByteBuffer.allocate(Character.BYTES).putChar(value).array();
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            return Arrays.copyOfRange(array, Character.BYTES - size, Character.BYTES);
        } else {
            return Arrays.copyOf(array, size);
        }
    }

    public static byte[] getAsByte(short value, int size, ByteOrder byteOrder) {
        if (size == 0) size = Short.BYTES;
        byte[] array = ByteBuffer.allocate(Short.BYTES).putShort(value).array();
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            return Arrays.copyOfRange(array, Short.BYTES - size, Short.BYTES);
        } else {
            return Arrays.copyOf(array, size);
        }
    }

    public static byte[] getAsByte(int value, int size, ByteOrder byteOrder) {
        if (size == 0) size = Integer.BYTES;
        byte[] array = ByteBuffer.allocate(Integer.BYTES).order(byteOrder).putInt(value).array();
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            return Arrays.copyOfRange(array, Integer.BYTES - size, Integer.BYTES);
        } else {
            return Arrays.copyOf(array, size);
        }
    }

    public static byte[] getAsByte(long value, int size, ByteOrder byteOrder) {
        if (size == 0) size = Long.BYTES;
        byte[] array = ByteBuffer.allocate(Long.BYTES).putLong(value).array();
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            return Arrays.copyOfRange(array, Long.BYTES - size, Long.BYTES);
        } else {
            return Arrays.copyOf(array, size);
        }
    }

    public static byte[] getAsByte(float value, int size, ByteOrder byteOrder) {
        if (size == 0) size = Float.BYTES;
        byte[] array = ByteBuffer.allocate(Float.BYTES).putFloat(value).array();
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            return Arrays.copyOfRange(array, Float.BYTES - size, Float.BYTES);
        } else {
            return Arrays.copyOf(array, size);
        }
    }

    public static byte[] getAsByte(double value, int size, ByteOrder byteOrder) {
        if (size == 0) size = Double.BYTES;
        byte[] array = ByteBuffer.allocate(Double.BYTES).putDouble(value).array();
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            return Arrays.copyOfRange(array, Double.BYTES - size, Double.BYTES);
        } else {
            return Arrays.copyOf(array, size);
        }
    }

    public static byte[] getAsByte(String value, int size) {
        return size == 0 ?
                value.getBytes() :
                Arrays.copyOfRange(value.getBytes(), 0, size);
    }


    public static byte[] getAsByte(boolean value) {
        return new byte[]{(byte) (value ? 1 : 0)};
    }
}
