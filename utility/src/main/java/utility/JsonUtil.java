package utility;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Json 처리를 원할하게 하기 위한 유틸
 */
public class JsonUtil {

    public static boolean isJson(String msg) {
        if (!msg.contains("{")) return false;
        Gson gson = new Gson();
        try {
            gson.fromJson(msg, Object.class);
            return true;
        } catch (com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }

    private JsonUtil() {
    }

    /**
     * Json 문자열을 받아 파싱하고 JsonElement 형태로 반환한다
     * ex)
     *    jsonStr = { "A" : {"B": { "C": "D"}}}
     *    element = ["A", "B"]
     *    return = (JsonElement) {"C":"D"}
     * @param jsonStr 파싱할 Json 문자열
     * @param element 파싱할 하위구조. element의 마지막 값을 JsonElement형태로 반환한다
     * @return 파싱한 JsonElement
     * @throws IllegalArgumentException
     */
    public static JsonElement json2Element(String jsonStr, String... element) {
        if (element.length == 0) throw new IllegalStateException("Element length must more than 0");
        Gson gson = new Gson();
        JsonObject jo = gson.fromJson(jsonStr, JsonObject.class);
        for (int i = 0; i < element.length - 1; i++) {
            jo = jo.getAsJsonObject(element[i]);
        }
        return jo.get(element[element.length - 1]);
    }

    /**
     * Json 문자열을 받아 파싱하고 int 형태로 반환한다
     * ex)
     *    jsonStr = { "A" : {"B": { "C": 123}}}
     *    element = ["A", "B", "C"]
     *    return = (int) 123
     * @param jsonStr 파싱할 Json 문자열
     * @param element 파싱할 하위구조. element의 마지막 값을 int로 반환한다
     * @return 파싱한 int
     * @throws IllegalArgumentException
     */
    public static int json2Int(String jsonStr, String... element) {
        return json2Element(jsonStr, element).getAsInt();
    }

    /**
     * Json 문자열을 받아 파싱하고 String 형태로 반환한다
     * ex)
     *    jsonStr = { "A" : {"B": { "C": "result"}}}
     *    element = ["A", "B", "C"]
     *    return = (String) "result"
     * @param jsonStr 파싱할 Json 문자열
     * @param element 파싱할 하위구조. element의 마지막 값을 String으로 반환한다
     * @return 파싱한 String
     * @throws IllegalArgumentException
     */
    public static String json2String(String jsonStr, String... element) {
        return json2Element(jsonStr, element).getAsString();
    }

    /**
     * Json 문자열을 받아 파싱하고 String 형태로 반환한다.
     * @param jsonStr 파싱할 Json 문자열
     * @param defaultValue 파싱이 실패할 경우 반환할 값.
     * @param element 파싱할 하위구조. element의 마지막 값을 String으로 반환한다
     * @return 파싱한 String. 파싱이 실패할 경우 defaultValue return
     */
    public static String json2StringWithDefault (String jsonStr, String defaultValue, String... element) {
        try{
            return json2Element(jsonStr, element).getAsString();
        } catch (Exception e){
            return defaultValue;
        }
    }

    /**
     * Json 문자열을 받아 파싱하고 boolean  형태로 반환한다
     * ex)
     *    jsonStr = { "A" : {"B": { "C": true}}}
     *    element = ["A", "B", "C"]
     *    return = (boolean) true
     * @param jsonStr 파싱할 Json 문자열
     * @param element 파싱할 하위구조. element의 마지막 값을 boolean으로 반환한다
     * @return 파싱한 boolean
     * @throws IllegalArgumentException
     */
    public static boolean json2Boolean(String jsonStr, String... element) {
        return json2Element(jsonStr, element).getAsBoolean();
    }

    /**
     * Json 문자열을 받아 파싱하고 float 형태로 반환한다
     * ex)
     *    jsonStr = { "A" : {"B": { "C": 3.14}}}
     *    element = ["A", "B", "C"]
     *    return = (float) 3.14
     * @param jsonStr 파싱할 Json 문자열
     * @param element 파싱할 하위구조. element의 마지막 값을 float으로 반환한다
     * @return 파싱한 float
     * @throws IllegalArgumentException
     */
    public static float json2Float(String jsonStr, String... element) {
        return json2Element(jsonStr, element).getAsFloat();
    }

    /**
     * Json 문자열을 받아 파싱하고 int[] 형태로 반환한다
     * ex)
     *    jsonStr = { "A" : {"B": { "C": [1,2,3]}}}
     *    element = ["A", "B", "C"]
     *    return = (int[]) {1,2,3}
     * @param jsonStr 파싱할 Json 문자열
     * @param element 파싱할 하위구조. element의 마지막 값을 int[]로 반환한다
     * @return 파싱한 int[]
     * @throws IllegalArgumentException
     */
    public static int[] json2IntArr(String jsonStr, String... element) {
        JsonElement jsonelement = json2Element(jsonStr, element);
        return jsonArr2IntArr(jsonelement.getAsJsonArray());
    }

    /**
     * Json 문자열을 받아 파싱하고 String[] 형태로 반환한다
     * ex)
     *    jsonStr = { "A" : {"B": { "C": ["aa","bb","cc"]}}}
     *    element = ["A", "B", "C"]
     *    return = (String[]) {"aa","bb","cc"}
     * @param jsonStr 파싱할 Json 문자열
     * @param element 파싱할 하위구조. element의 마지막 값을 String[]로 반환한다
     * @return 파싱한 String[]
     * @throws IllegalArgumentException
     */
    public static String[] json2StringArr(String jsonStr, String... element) {
        JsonElement jsonelement = json2Element(jsonStr, element);
        return jsonArr2StringArr(jsonelement.getAsJsonArray());
    }

    /**
     * Json 문자열을 받아 파싱하고 int[] 형태로 반환한다
     * ex)
     *    jsonStr = { "A" : {"B": { "C": [1.1, 2.2, 3.3]}}}
     *    element = ["A", "B", "C"]
     *    return = (float[]) {1.1, 2.2, 3.3}
     * @param jsonStr 파싱할 Json 문자열
     * @param element 파싱할 하위구조. element의 마지막 값을 float[]로 반환한다
     * @return 파싱한 float[]
     * @throws IllegalArgumentException
     */
    public static float[] json2FloatArr(String jsonStr, String... element) {
        JsonElement jsonelement = json2Element(jsonStr, element);
        return jsonArr2FloatArr(jsonelement.getAsJsonArray());
    }

    /**
     * Json 문자열을 받아 파싱하고 boolean[] 형태로 반환한다
     * ex)
     *    jsonStr = { "A" : {"B": { "C": [true, false, true]}}}
     *    element = ["A", "B", "C"]
     *    return = (boolean[]) {true, false, true}
     * @param jsonStr 파싱할 Json 문자열
     * @param element 파싱할 하위구조. element의 마지막 값을 boolean[]로 반환한다
     * @return 파싱한 boolean[]
     * @throws IllegalArgumentException
     */
    public static boolean[] json2BooleanArr(String jsonStr, String... element) {
        JsonElement jsonelement = json2Element(jsonStr, element);
        return jsonArr2BooleanArr(jsonelement.getAsJsonArray());
    }

    /**
     * JsonArray를 받아 int[]로 반환한다
     * @param jsonArray
     * @return
     */
    public static int[] jsonArr2IntArr(JsonArray jsonArray) {
        int[] ret = new int[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            ret[i] = jsonArray.get(i).getAsInt();
        }
        return ret;
    }

    /**
     * JsonArray를 받아 String[]로 반환한다
     * @param jsonArray
     * @return
     */
    public static String[] jsonArr2StringArr(JsonArray jsonArray) {
        String[] ret = new String[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            ret[i] = jsonArray.get(i).getAsString();
        }
        return ret;
    }

    /**
     * JsonArray를 받아 boolean[]로 반환한다
     * @param jsonArray
     * @return
     */
    public static boolean[] jsonArr2BooleanArr(JsonArray jsonArray) {
        boolean[] ret = new boolean[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            ret[i] = jsonArray.get(i).getAsBoolean();
        }
        return ret;
    }

    /**
     * JsonArray를 받아 float[]로 반환한다
     * @param jsonArray
     * @return
     */
    public static float[] jsonArr2FloatArr(JsonArray jsonArray) {
        float[] ret = new float[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            ret[i] = jsonArray.get(i).getAsFloat();
        }
        return ret;
    }
}
