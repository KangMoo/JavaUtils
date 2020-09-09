import utility.JsonUtil;

public class Tester {
    public void test1(){
        System.out.println(JsonUtil.json2String("\"A\":\"B\"","A"));
        System.out.println(JsonUtil.json2String("\"A\":\"B\"","B"));
    }
}

