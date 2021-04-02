package test;

import org.junit.Test;
import utility.JsonUtil;

import java.util.Arrays;

/**
 *
 * @author kangmoo Heo
 */
public class JsonUtilTest {
    String jsonSample = "" +
            "{\n" +
            "  \"name\": \"Kang Moo\",\n" +
            "  \"age\": 28,\n" +
            "  \"male\": true,\n" +
            "  \"tree\": {\n" +
            "    \"stem\": {\n" +
            "      \"branch1\": {\n" +
            "        \"branchSize\": 22.5,\n" +
            "        \"leafSize\": [1.1,2.0,2.5]\n" +
            "      },\n" +
            "      \"branch2\": {\n" +
            "        \"branchSize\": 33,\n" +
            "        \"leafSize\": [3.7,1.0,2.2]\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    @Test
    public void jsonUtilTest() {
        if (JsonUtil.isJson(jsonSample)) {
            System.out.println(JsonUtil.json2String(jsonSample, "name"));
            System.out.println(JsonUtil.json2Int(jsonSample, "age"));
            System.out.println(JsonUtil.json2Boolean(jsonSample, "male"));
            System.out.println(JsonUtil.json2Float(jsonSample, "tree", "stem", "branch1", "branchSize"));
            System.out.println(Arrays.toString(JsonUtil.json2FloatArr(jsonSample, "tree", "stem", "branch2", "leafSize")));
        }

        try {
            System.out.println(JsonUtil.json2Float(jsonSample, "WrongParam"));
        } catch (NullPointerException e) {
            System.out.println("If param Wrong. Throw [NullPointerException] Exception");
        }
    }
}
