package com.github.kangmoo.utils.config.json;

import com.github.kangmoo.utils.config.ConfigValue;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author kangmoo Heo
 */
@Slf4j
@ToString
public class JsonConfigInjector {
    private static final Gson gson = new Gson();

    private JsonConfigInjector() {
    }

    public static void inject(Object target, String jsonFilePath) throws NoSuchFieldException, IOException {
        injectValues(target, Files.readString(Path.of(jsonFilePath)));
    }

    public static void injectValues(Object target, String jsonString) throws NoSuchFieldException {
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        Class<?> clazz = target.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(ConfigValue.class)) {
                continue;
            }

            ConfigValue configValue = field.getAnnotation(ConfigValue.class);
            String[] propertyKeys = configValue.value();
            try {
                field.setAccessible(true);
                JsonElement val = findElement(jsonObject, propertyKeys).orElse(null);
                if (val == null) {
                    continue;
                }

                Type fieldType = field.getGenericType();
                Object o = gson.fromJson(val, fieldType);
                field.set(target, o);
                log.info("Config injected. [{}.{}] <- [{}]", target.getClass().getSimpleName(), field.getName(), field.get(target));
            } catch (Exception e) {
                NoSuchFieldException ex = new NoSuchFieldException("Fail to inject value. [Field : " + field.getName() + "], [ConfigValue : " + Arrays.toString(propertyKeys) + "]");
                ex.initCause(e);
                throw ex;
            } finally {
                field.setAccessible(false);
            }
        }
    }

    private static Optional<JsonElement> findElement(JsonElement jsonElement, String... keys) {
        JsonElement currentElement = jsonElement;

        for (String key : keys) {
            if (currentElement.isJsonObject()) {
                JsonObject jsonObject = currentElement.getAsJsonObject();
                if (jsonObject.has(key)) {
                    currentElement = jsonObject.get(key);
                } else {
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }
        }
        return Optional.of(currentElement);
    }


}
