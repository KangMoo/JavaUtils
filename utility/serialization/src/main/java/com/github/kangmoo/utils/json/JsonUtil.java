package com.github.kangmoo.utils.json;

import com.google.gson.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

/**
 * Json 처리를 원할하게 하기 위한 유틸
 */
public class JsonUtil {
    private static final Pattern arrayPattern = Pattern.compile("^(.*)\\[(\\d+)]$");

    private JsonUtil() {
    }

    public static Optional<JsonObject> json2Object(String jsonStr, String... elements) {
        try {
            return json2JsonElement(jsonStr, elements)
                    .map(JsonElement::getAsJsonObject);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<JsonArray> json2Array(String jsonStr, String... elements) {
        try {
            return json2JsonElement(jsonStr, elements)
                    .map(JsonElement::getAsJsonArray);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<JsonPrimitive> json2Primitive(String jsonStr, String... elements) {
        try {
            return json2JsonElement(jsonStr, elements)
                    .map(JsonElement::getAsJsonPrimitive);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<JsonNull> json2Null(String jsonStr, String... elements) {
        try {
            return json2JsonElement(jsonStr, elements)
                    .map(JsonElement::getAsJsonNull);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<Boolean> json2Boolean(String jsonStr, String... elements) {
        try {
            return json2JsonElement(jsonStr, elements)
                    .map(JsonElement::getAsBoolean);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<Number> json2Number(String jsonStr, String... elements) {
        try {
            return json2JsonElement(jsonStr, elements)
                    .map(JsonElement::getAsNumber);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<String> json2String(String jsonStr, String... elements) {
        try {
            return json2JsonElement(jsonStr, elements)
                    .map(JsonElement::getAsString);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<Double> json2Double(String jsonStr, String... elements) {
        try {
            return json2JsonElement(jsonStr, elements)
                    .map(JsonElement::getAsDouble);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<Float> json2Float(String jsonStr, String... elements) {
        try {
            return json2JsonElement(jsonStr, elements)
                    .map(JsonElement::getAsFloat);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<Long> json2Long(String jsonStr, String... elements) {
        try {
            return json2JsonElement(jsonStr, elements)
                    .map(JsonElement::getAsLong);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<Integer> json2Int(String jsonStr, String... elements) {
        try {
            return json2JsonElement(jsonStr, elements)
                    .map(JsonElement::getAsInt);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<Byte> json2Byte(String jsonStr, String... elements) {
        try {
            return json2JsonElement(jsonStr, elements)
                    .map(JsonElement::getAsByte);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<BigDecimal> json2BigDecimal(String jsonStr, String... elements) {
        try {
            return json2JsonElement(jsonStr, elements)
                    .map(JsonElement::getAsBigDecimal);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<BigInteger> json2BigInteger(String jsonStr, String... elements) {
        try {
            return json2JsonElement(jsonStr, elements)
                    .map(JsonElement::getAsBigInteger);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<Short> json2Short(String jsonStr, String... elements) {
        try {
            return json2JsonElement(jsonStr, elements)
                    .map(JsonElement::getAsShort);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static boolean isJson(String msg) {
        if (!msg.contains("{")) return false;
        Gson gson = new Gson();
        try {
            gson.fromJson(msg, Object.class);
            return true;
        } catch (JsonSyntaxException ex) {
            return false;
        }
    }

    public static Optional<JsonElement> json2JsonElement(String jsonStr, String... element) {
        return findJsonElement(new Gson().fromJson(jsonStr, JsonElement.class), element);
    }

    public static Optional<JsonElement> findJsonElement(JsonElement jsonElement, String... element) {
        if (element.length == 0) return Optional.ofNullable(jsonElement);
        Optional<JsonElement> optionalJsonObject = Optional.ofNullable(jsonElement);
        for (String property : element) {
            optionalJsonObject = optionalJsonObject
                    .map(JsonElement::getAsJsonObject)
                    .map(o -> {
                        Matcher matcher = arrayPattern.matcher(property);
                        if (matcher.find()) {
                            String key = matcher.group(1);
                            int index = Integer.parseInt(matcher.group(2));
                            return o.getAsJsonArray(key).get(index);
                        }
                        return o.get(property);
                    });
        }
        return optionalJsonObject;
    }

    public static List<JsonElement> jsonArray2jsonElementList(JsonArray jsonArray) {
        return StreamSupport.stream(jsonArray.spliterator(), false).toList();
    }

    public static Optional<String> toPrettyJson(String json) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement je = JsonParser.parseString(json);
            return Optional.of(gson.toJson(je));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static String toJson(Object object) {
        return new GsonBuilder().serializeNulls().create().toJson(object);
    }
}
