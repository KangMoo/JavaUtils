package com.github.kangmoo.utils.config.yaml;

/**
 * @author kangmoo Heo
 */

import com.github.kangmoo.utils.config.ConfigValue;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;

@Slf4j
public class YamlConfigInjector {

    private YamlConfigInjector() {
    }

    public static void inject(Object target, String yamlFilePath) throws NoSuchFieldException, IOException {
        Map<String, Object> yamlData = readYamlFile(yamlFilePath);
        injectValues(target, yamlData);
    }

    private static Map<String, Object> readYamlFile(String yamlFilePath) throws IOException {
        Yaml yaml = new Yaml();
        InputStream inputStream = new FileInputStream(yamlFilePath);
        return yaml.load(inputStream);
    }

    private static void injectValues(Object target, Map<String, Object> yamlData) throws NoSuchFieldException {
        Class<?> clazz = target.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ConfigValue.class)) {
                ConfigValue configValue = field.getAnnotation(ConfigValue.class);
                String propertyKey = configValue.value();
                Object propertyValue = getProperty(yamlData, propertyKey);
                if (propertyValue != null) {
                    field.setAccessible(true);
                    try {
                        if (field.getType().equals(String.class)) {
                            field.set(target, String.valueOf(propertyValue));
                        } else {
                            field.set(target, propertyValue);
                        }
                        try {
                            log.info("Config injected. [{}.{}] <- [{}]", target.getClass().getSimpleName(), field.getName(), field.get(target));
                        } catch (Exception e) {
                            // Do nothing
                        }
                    } catch (Exception e) {
                        NoSuchFieldException exception = new NoSuchFieldException("Fail to inject value. [Field : " + field.getName() + "], [ConfigValue : " + propertyKey + "]");
                        exception.initCause(e);
                        throw exception;
                    } finally {
                        field.setAccessible(false);
                    }
                }
            }
        }
    }

    private static Object getProperty(Map<String, Object> yamlData, String propertyKey) {
        String[] keys = propertyKey.split("\\.");
        Map<String, Object> currentMap = yamlData;
        for (int i = 0; i < keys.length - 1; i++) {
            currentMap = (Map<String, Object>) currentMap.get(keys[i]);
            if (currentMap == null) {
                return null;
            }
        }
        return currentMap.get(keys[keys.length - 1]);
    }
}
