package com.github.kangmoo.utils.config.ini;

/**
 * @author kangmoo Heo
 */

import com.github.kangmoo.utils.config.ConfigValue;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;

public class IniConfigInjector {

    private IniConfigInjector() {
    }

    public static void inject(Object targetObject, String iniFilePath) throws IOException, NoSuchFieldException {
        Ini ini = new Ini(new File(iniFilePath));
        Class<?> targetClass = targetObject.getClass();
        Field[] fields = targetClass.getDeclaredFields();

        for (Field field : fields) {
            ConfigValue annotation = field.getAnnotation(ConfigValue.class);
            if (Objects.nonNull(annotation)) {
                String configKey = annotation.value();
                String[] tokens = configKey.split("\\.");
                String sectionName = tokens[0];
                String optionName = tokens[1];

                Section section = ini.get(sectionName);
                String value = section.get(optionName);
                if (value != null) {
                    field.setAccessible(true);
                    try {
                        Class<?> fieldType = field.getType();
                        if (String.class.equals(fieldType)) {
                            field.set(targetObject, value);
                        } else if (int.class.equals(fieldType) || Integer.class.equals(fieldType)) {
                            field.set(targetObject, Integer.parseInt(value));
                        } else if (long.class.equals(fieldType) || Long.class.equals(fieldType)) {
                            field.set(targetObject, Long.parseLong(value));
                        } else if (double.class.equals(fieldType) || Double.class.equals(fieldType)) {
                            field.set(targetObject, Double.parseDouble(value));
                        } else if (float.class.equals(fieldType) || Float.class.equals(fieldType)) {
                            field.set(targetObject, Float.parseFloat(value));
                        } else if (boolean.class.equals(fieldType) || Boolean.class.equals(fieldType)) {
                            field.set(targetObject, Boolean.parseBoolean(value));
                        } else {
                            throw new UnsupportedOperationException("Unsupported field type: " + fieldType);
                        }
                    } catch(Exception e){
                        NoSuchFieldException exception = new NoSuchFieldException("Fail to inject value. [Field : " + field.getName() + "], [ConfigValue : " + configKey + "]");
                        exception.initCause(e);
                        throw exception;
                    } finally {
                        field.setAccessible(false);
                    }
                }
            }
        }
    }
}
