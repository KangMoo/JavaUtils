package utility;

import com.google.gson.*;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class JsonMaker {
    private final Map<String, JsonElement> members = new LinkedHashMap<>();

    public JsonObject deepCopyJsonObj() {
        JsonObject result = new JsonObject();
        Iterator var2 = this.members.entrySet().iterator();

        while (var2.hasNext()) {
            Map.Entry<String, JsonElement> entry = (Map.Entry) var2.next();
            result.add(entry.getKey(), (entry.getValue()).deepCopy());
        }

        return result;
    }

    public JsonMaker deepCopy() {
        JsonMaker result = new JsonMaker();
        Iterator var2 = this.members.entrySet().iterator();
        while (var2.hasNext()) {
            Map.Entry<String, JsonElement> entry = (Map.Entry) var2.next();
            result.add(entry.getKey(), (entry.getValue()).deepCopy());
        }
        return result;
    }

    public JsonMaker add(String property, JsonElement value) {
        this.members.put(property, value == null ? JsonNull.INSTANCE : value);
        return this;
    }

    public JsonElement remove(String property) {
        return this.members.remove(property);
    }

    public JsonMaker addProperty(String property, String value) {
        this.add(property, (value == null ? JsonNull.INSTANCE : new JsonPrimitive(value)));
        return this;
    }

    public JsonMaker addProperty(String property, Number value) {
        this.add(property, (value == null ? JsonNull.INSTANCE : new JsonPrimitive(value)));
        return this;
    }

    public JsonMaker addProperty(String property, Boolean value) {
        this.add(property, (value == null ? JsonNull.INSTANCE : new JsonPrimitive(value)));
        return this;
    }

    public JsonMaker addProperty(String property, Character value) {
        this.add(property, (value == null ? JsonNull.INSTANCE : new JsonPrimitive(value)));
        return this;
    }

    public Set<Map.Entry<String, JsonElement>> entrySet() {
        return this.members.entrySet();
    }

    public Set<String> keySet() {
        return this.members.keySet();
    }

    public int size() {
        return this.members.size();
    }

    public boolean has(String memberName) {
        return this.members.containsKey(memberName);
    }

    public JsonElement get(String memberName) {
        return this.members.get(memberName);
    }

    public JsonPrimitive getAsJsonPrimitive(String memberName) {
        return (JsonPrimitive) this.members.get(memberName);
    }

    public JsonArray getAsJsonArray(String memberName) {
        return (JsonArray) this.members.get(memberName);
    }

    public JsonObject getAsJsonObject(String memberName) {
        return (JsonObject) this.members.get(memberName);
    }

    public boolean equals(Object o) {
        return o == this || o instanceof JsonObject && ((JsonMaker) o).members.equals(this.members);
    }

    public int hashCode() {
        return this.members.hashCode();
    }
}