package com.github.kangmoo.utils.binary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.github.kangmoo.utils.binary.ByteDataConverter.getAsByte;

/**
 * @author kangmoo Heo
 */
@Slf4j
public class SpanConverter {
    private SpanConverter() {
    }

    public static <T> T parse(byte[] data, Class<T> classOfT) {
        try {
            Object target = ObjectInitializer.createInstance(classOfT);
            Class<?> clazz = target.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Span.class)) {
                    Span spanValue = field.getAnnotation(Span.class);
                    int offset = spanValue.offset();
                    int size = spanValue.size();
                    Span.SpanByteOrder order = spanValue.order();
                    ByteOrder byteOrder = order == Span.SpanByteOrder.BIG_ENDIAN ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
                    Class<?> fieldType = field.getType();

                    if (fieldType == char.class || fieldType == Character.class) {
                        setField(target, field, ByteDataConverter.getAsChar(data, offset, size, byteOrder));
                    } else if (fieldType == short.class || fieldType == Short.class) {
                        setField(target, field, ByteDataConverter.getAsShort(data, offset, size, byteOrder));
                    } else if (fieldType == int.class || fieldType == Integer.class) {
                        setField(target, field, ByteDataConverter.getAsInt(data, offset, size, byteOrder));
                    } else if (fieldType == long.class || fieldType == Long.class) {
                        setField(target, field, ByteDataConverter.getAsLong(data, offset, size, byteOrder));
                    } else if (fieldType == float.class || fieldType == Float.class) {
                        setField(target, field, ByteDataConverter.getAsFloat(data, offset, size, byteOrder));
                    } else if (fieldType == double.class || fieldType == Double.class) {
                        setField(target, field, ByteDataConverter.getAsDouble(data, offset, size, byteOrder));
                    } else if (fieldType == String.class) {
                        setField(target, field, ByteDataConverter.getAsString(data, offset, size));
                    } else {
                        Object value = getField(target, field);
                        if (value == null) {
                            value = ObjectInitializer.createInstance(field.getType());
                        }
                        parse(data, fieldType);
                        setField(target, field, value);
                    }
                }
            }
            return classOfT.cast(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setField(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            log.warn("Err Occurs", e);
        } finally {
            field.setAccessible(false);
        }
    }

    private static Object getField(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            field.setAccessible(false);
        }
    }

    public static byte[] toByteArray(Object target) {
        Class<?> clazz = target.getClass();

        Set<SpanData> spanDatas = new HashSet<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Span.class)) {
                Span spanValue = field.getAnnotation(Span.class);
                spanDatas.add(new SpanData(field, spanValue.offset(), spanValue.size(), (spanValue.order() == Span.SpanByteOrder.BIG_ENDIAN ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN)));
            }
        }


        Map<Integer, byte[]> datas = new HashMap<>();

        spanDatas.forEach(spanData -> {
            Field field = spanData.field;
            int offset = spanData.offset;
            int size = spanData.size;
            ByteOrder byteOrder = spanData.getByteOrder();
            Class<?> fieldType = field.getType();

            Object value = getField(target, field);
            if (fieldType == char.class || fieldType == Character.class) {
                datas.put(offset, getAsByte((Character) value, size, byteOrder));
            } else if (fieldType == short.class || fieldType == Short.class) {
                datas.put(offset, getAsByte((Short) value, size, byteOrder));
            } else if (fieldType == int.class || fieldType == Integer.class) {
                datas.put(offset, getAsByte((Integer) value, size, byteOrder));
            } else if (fieldType == long.class || fieldType == Long.class) {
                datas.put(offset, getAsByte((Long) value, size, byteOrder));
            } else if (fieldType == float.class || fieldType == Float.class) {
                datas.put(offset, getAsByte((Float) value, size, byteOrder));
            } else if (fieldType == double.class || fieldType == Double.class) {
                datas.put(offset, getAsByte((Double) value, size, byteOrder));
            } else if (fieldType == String.class) {
                datas.put(offset, getAsByte((String) value, size));
            } else {
                datas.put(offset, toByteArray(value));
            }
        });

        int byteSize = datas.entrySet().stream().map(entry -> entry.getKey() + entry.getValue().length)
                .mapToInt(o -> o)
                .max()
                .orElseThrow();

        ByteBuffer bf = ByteBuffer.wrap(new byte[byteSize]);
        datas.forEach((key, value) -> {
            bf.position(key);
            bf.put(value);
        });
        bf.flip();
        return bf.array();
    }

    @Data
    @AllArgsConstructor
    private static class SpanData {
        Field field;
        int offset;
        int size;
        ByteOrder byteOrder;
    }
}
