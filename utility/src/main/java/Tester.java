import com.google.gson.Gson;
import com.google.gson.JsonObject;
import utility.JsonMaker;
import utility.MsgLogger;

public class Tester {
    public void MsgLoggerTest() {
        MsgLogger.start("$HOME/msglog");
        MsgLogger.update("A1","B1","WHT1","CONTENT!!");
        MsgLogger.update("A1","B2","WHT2","CONTENT!asd!");
        MsgLogger.update("B1","B2","WHT3","CONTENT!!f");
        MsgLogger.update("B2","A1","WHT4","CONTENT!bhewgvb!");
    }
}
