package com.github.kangmoo.utils.config;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Objects;

@Slf4j
public class SysEnvInjector {
    public static void inject(Object target) {
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            SysEnv annotation = field.getAnnotation(SysEnv.class);
            if (Objects.isNull(annotation)) {
                continue;
            }
            String envVal = System.getenv(annotation.value());
            if (envVal == null || envVal.isEmpty()) {
                continue;
            }

            field.setAccessible(true);
            try {
                Class<?> fieldType = field.getType();

                if (String.class.equals(fieldType)) {
                    field.set(target, envVal);
                } else if (char.class.equals(fieldType) || Character.class.equals(fieldType)) {
                    field.set(target, envVal.charAt(0));
                } else if (byte.class.equals(fieldType) || Byte.class.equals(fieldType)) {
                    field.set(target, Byte.parseByte(envVal));
                } else if (short.class.equals(fieldType) || Short.class.equals(fieldType)) {
                    field.set(target, Short.parseShort(envVal));
                } else if (int.class.equals(fieldType) || Integer.class.equals(fieldType)) {
                    field.set(target, Integer.parseInt(envVal));
                } else if (long.class.equals(fieldType) || Long.class.equals(fieldType)) {
                    field.set(target, Long.parseLong(envVal));
                } else if (double.class.equals(fieldType) || Double.class.equals(fieldType)) {
                    field.set(target, Double.parseDouble(envVal));
                } else if (float.class.equals(fieldType) || Float.class.equals(fieldType)) {
                    field.set(target, Float.parseFloat(envVal));
                } else if (boolean.class.equals(fieldType) || Boolean.class.equals(fieldType)) {
                    field.set(target, Boolean.parseBoolean(envVal));
                } else {
                    throw new UnsupportedOperationException("Unsupported field type: " + fieldType);
                }
                try {
                    log.info("Config injected. [{}.{}] <- [{}]", target.getClass().getSimpleName(), field.getName(), field.get(target));
                } catch (Exception e) {
                    // Do nothing
                }
            } catch (Exception e) {
                NoSuchFieldException exception = new NoSuchFieldException("Fail to inject value. [Field : " + field.getName() + "], [ConfigValue : " + envVal + "]");
                exception.initCause(e);
                log.warn("Fail to inject env config", exception);
            } finally {
                field.setAccessible(false);
            }
        }
    }
}
