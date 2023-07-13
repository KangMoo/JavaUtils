package com.github.kangmoo.utils.config.ini;

/**
 * @author kangmoo Heo
 */

import com.github.kangmoo.utils.config.ConfigValue;
import lombok.extern.slf4j.Slf4j;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;

@Slf4j
public class IniConfigInjector {

    private IniConfigInjector() {
    }

    public static void inject(Object target, String iniFilePath) throws IOException, NoSuchFieldException {
        Ini ini = new Ini(new File(iniFilePath));
        Field[] fields = target.getClass().getDeclaredFields();

        for (Field field : fields) {
            ConfigValue annotation = field.getAnnotation(ConfigValue.class);
            if (Objects.nonNull(annotation)) {
                String configKey = annotation.value();
                String[] tokens = configKey.split("\\.");
                String sectionName = tokens[0];
                String optionName = tokens[1];

                Section section = ini.get(sectionName);
                if (section == null) continue;
                String value = section.get(optionName);
                if (value == null) continue;

                field.setAccessible(true);
                try {
                    Class<?> fieldType = field.getType();
                    if (String.class.equals(fieldType)) {
                        field.set(target, value);
                    } else if (int.class.equals(fieldType) || Integer.class.equals(fieldType)) {
                        field.set(target, Integer.parseInt(value));
                    } else if (long.class.equals(fieldType) || Long.class.equals(fieldType)) {
                        field.set(target, Long.parseLong(value));
                    } else if (double.class.equals(fieldType) || Double.class.equals(fieldType)) {
                        field.set(target, Double.parseDouble(value));
                    } else if (float.class.equals(fieldType) || Float.class.equals(fieldType)) {
                        field.set(target, Float.parseFloat(value));
                    } else if (boolean.class.equals(fieldType) || Boolean.class.equals(fieldType)) {
                        field.set(target, Boolean.parseBoolean(value));
                    } else {
                        throw new UnsupportedOperationException("Unsupported field type: " + fieldType);
                    }
                    try {
                        log.info("Config injected. [{}.{}] <- [{}]", target.getClass().getSimpleName(), field.getName(), field.get(target));
                    } catch (Exception e) {
                        // Do nothing
                    }
                } catch (Exception e) {
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
