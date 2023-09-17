package com.github.kangmoo.utils.binary;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * @author kangmoo Heo
 */
@Slf4j
public class SpanInjector {
    private SpanInjector() {
    }

    public static void inject(Object target, byte[] data) {
        Class<?> clazz = target.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Span.class)) {
                try {
                    field.setAccessible(true);
                    Span spanValue = field.getAnnotation(Span.class);
                    int offset = spanValue.offset();
                    int size = spanValue.size();
                    Class<?> fieldType = field.getType();

                    if (fieldType == char.class || fieldType == Character.class) {
                        field.set(target, ByteDataExtractor.getAsChar(data, offset, size));
                    } else if (fieldType == short.class || fieldType == Short.class) {
                        field.set(target, ByteDataExtractor.getAsShort(data, offset, size));
                    } else if (fieldType == int.class || fieldType == Integer.class) {
                        field.set(target, ByteDataExtractor.getAsInt(data, offset, size));
                    } else if (fieldType == long.class || fieldType == Long.class) {
                        field.set(target, ByteDataExtractor.getAsLong(data, offset, size));
                    } else if (fieldType == float.class || fieldType == Float.class) {
                        field.set(target, ByteDataExtractor.getAsFloat(data, offset, size));
                    } else if (fieldType == double.class || fieldType == Double.class) {
                        field.set(target, ByteDataExtractor.getAsDouble(data, offset, size));
                    } else if (fieldType == String.class) {
                        field.set(target, ByteDataExtractor.getAsString(data, offset, size));
                    } else {
                        Object obj = field.get(target);
                        if (obj == null) {
                            obj = ObjectInitializer.createInstance(field.getType());
                        }
                        inject(obj, data);
                        field.set(target, obj);
                    }
                } catch (Exception e) {
                    log.warn("Err Occurs", e);
                } finally {
                    field.setAccessible(false);
                }
            }
        }
    }
}
