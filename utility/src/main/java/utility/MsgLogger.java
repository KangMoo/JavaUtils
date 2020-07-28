package utility;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

// Use at https://sequencediagram.org/
public class MsgLogger implements Runnable{
    private static MsgLogger instance;
    private static String filePath;
    private static int timer;
    private static String fileDateFormat;
    private static String logDateFormat;
    private static StringBuilder recorder;
    private static Runtime runtime = Runtime.getRuntime();
    private static Thread thread;
    private static boolean isQuit = false;


    private MsgLogger(){}

    public static void start(String filePath){
        MsgLogger.start(filePath, 10, "yyyyMMdd", "yyyy-MM-dd - HH:mm:ss:SSS");
    }

    public static void start(String filePath, int timer, String fileDateFormat, String logDateFormat){
        MsgLogger.filePath = filePath;
        MsgLogger.fileDateFormat = fileDateFormat;
        MsgLogger.logDateFormat = logDateFormat;
        MsgLogger.timer = timer;
        MsgLogger.recorder = new StringBuilder();
        runtime.addShutdownHook(new Thread(() -> {
            MsgLogger.record();
        }));
        thread = new Thread(MsgLogger.instance);
        thread.start();
    }

    @Override
    public void run() {
        while(!isQuit){
            try{
                Thread.sleep(this.timer * 1000);
                record();
            }catch(Exception e){
                continue;
            }
        }
        record();
    }



    public static void update(String from, String to, String name, String content){
        String now =  new SimpleDateFormat(logDateFormat).format(new Date());
        synchronized (recorder){
            recorder.append(from).append("->").append(to)
                    .append(": [").append(now).append("] ")
                    .append(name).append("\n").append(content).append("\n");
        }
    }

    private static void record(){
        String now =  new SimpleDateFormat(fileDateFormat).format(new Date());
        synchronized (recorder){
            if(recorder.length()<=0) return;
            fileAppendWrite(getStringWithEnv(filePath+now), recorder.toString(),"");
            recorder = new StringBuilder();
        }
    }

    /**
     * 파일에 문자열을 덧붙인다.
     * @param filePath 파일 경로
     * @param msg 덧붙일 메시지
     * @param delimiter 구분문자
     * @return
     */
    public static boolean fileAppendWrite(String filePath,String msg, String delimiter)
    {
        File f = new File(filePath);
        // 파일이 없으면 경로 및 파일 생성
        if(!f.exists()){
            try{
                FileUtil.createFileWithDirectory(filePath);
            }catch(Exception e){
                return false;
            }
        }
        PrintStream out = null;

        try {
            out = new PrintStream(new FileOutputStream(f, true));
            out.print(msg);
            out.print(delimiter);
        } catch ( FileNotFoundException e ) {
            return false;
        }
        finally {
            if ( out != null ) {
                out.close( );
            }
        }
        return true;
    }

    /**
     * 문자열의 환경변수 이름을 매핑한다.
     * ex) "$HOME" -> "/home/"
     * @param str 매핑할 문자열
     * @return 환경변수가 매핑된 문자열
     */
    public static String getStringWithEnv(String str){
        Map<String, String> envMap = System.getenv();
        String ret = str;
        for(String envKey : envMap.keySet()){
            String env = "$"+ envKey;
            if(str.contains(env)) ret = str.replace(env,envMap.get(envKey));
        }
        return ret;
    }
}
