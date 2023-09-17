package com.github.kangmoo.utils.binary;

import java.lang.reflect.Constructor;

/**
 * @author kangmoo Heo
 */
public class ObjectInitializer {
    private ObjectInitializer() {
    }

    public static Object createInstance(Class<?> clazz) throws Exception {
        // 1. Try the default constructor first
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            // Ignore, move to the next approach
        }

        // 2. Try other constructors with parameters
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            try {
                constructor.setAccessible(true);
                Class<?>[] paramTypes = constructor.getParameterTypes();
                Object[] params = new Object[paramTypes.length];

                for (int i = 0; i < paramTypes.length; i++) {
                    params[i] = createDefaultInstance(paramTypes[i]);
                }

                return constructor.newInstance(params);
            } catch (Exception e) {
                // Ignore, move to the next constructor
            }
        }

        // If all methods fail
        throw new Exception("Unable to create instance for class: " + clazz.getName());
    }

    private static Object createDefaultInstance(Class<?> clazz) throws Exception {
        // Handle common types
        if (clazz.equals(int.class) || clazz.equals(Integer.class)) return 0;
        if (clazz.equals(long.class) || clazz.equals(Long.class)) return 0L;
        if (clazz.equals(double.class) || clazz.equals(Double.class)) return 0.0;
        if (clazz.equals(float.class) || clazz.equals(Float.class)) return 0.0f;
        if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) return false;
        if (clazz.equals(char.class) || clazz.equals(Character.class)) return '\0';
        if (clazz.equals(short.class) || clazz.equals(Short.class)) return (short) 0;
        if (clazz.equals(byte.class) || clazz.equals(Byte.class)) return (byte) 0;
        if (clazz.equals(String.class)) return "";

        // For other types, try to create a new instance
        return createInstance(clazz);
    }
}
