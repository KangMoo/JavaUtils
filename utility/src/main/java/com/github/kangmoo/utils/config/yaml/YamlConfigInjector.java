package com.github.kangmoo.utils.config.yaml;

/**
 * @author kangmoo Heo
 */

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;

public class YamlConfigInjector {

    private YamlConfigInjector() {
    }

    public static Object inject(Object target, String yamlFilePath) throws NoSuchFieldException, FileNotFoundException {
        Map<String, Object> yamlData = readYamlFile(yamlFilePath);
        injectValues(target, yamlData);
        return target;
    }

    private static Map<String, Object> readYamlFile(String yamlFilePath) throws FileNotFoundException {
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
                    } catch (Exception e) {
                        NoSuchFieldException exception = new NoSuchFieldException("Fail to inject value. [Field : " + field.getName() + "], [ConfigValue : " + propertyKey + "]");
                        exception.initCause(e);
                        throw exception;
                    }
                    field.setAccessible(false);
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
