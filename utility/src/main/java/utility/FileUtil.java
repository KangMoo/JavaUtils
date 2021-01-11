package utility;

import java.io.*;
import java.util.Map;

/**
 * 파일을 읽고 String으로 변환해주는 유틸
 */
public class FileUtil {

    private FileUtil() {
    }

    /**
     * 파일 경로를 문자열로 받고, 경로에 해당하는 파일 읽은 후 내용을 문자열로 전달
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static String filepathToString(String path) throws IOException {
        return fileToString(new File(path));
    }

    /**
     * 파일 읽은 후 내용을 문자열로 전달
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String fileToString(File file) throws IOException {
        return inputStreamToString(new FileInputStream(file));
    }

    /**
     * Stream -> String변환 함수
     *
     * @param is InputStream
     * @return String으로 캐스팅된 InputStream
     * @throws IOException
     */
    public static String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    public static File createFileWithDirectory(String filePath) throws Exception {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        if(file.createNewFile()){
            return file;
        }
        return null;
    }

    /**
     * 문자열의 환경변수 이름을 매핑한다.
     * ex) "$HOME" -> "/home/"
     *
     * @param str 매핑할 문자열
     * @return 환경변수가 매핑된 문자열
     */
    public static String getStringWithEnv(String str) {
        Map<String, String> envMap = System.getenv();
        String ret = str;
        for (String envKey : envMap.keySet()) {
            String env = "$" + envKey;
            if (str.contains(env)) ret = str.replace(env, envMap.get(envKey));
        }
        return ret;
    }

    /**
     * 파일에 문자열을 덧붙인다.
     *
     * @param filePath 파일 경로
     * @param msg      덧붙일 메시지
     * @return
     */
    public static boolean fileWrite(String filePath, String msg, boolean append) throws Exception {
        File f = new File(filePath);
        // 파일이 없으면 경로 및 파일 생성
        if (!f.exists()) {
            FileUtil.createFileWithDirectory(filePath);
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(f, append));  //Set true for append mode
        writer.write(msg);
        writer.close();

        return true;
    }

    /**
     * 파일에 문자열을 덧붙인다.
     *
     * @param filePath 파일 경로
     * @param data     덧붙일 byte
     * @return
     */
    public static boolean fileWrite(String filePath, byte[] data, boolean append) throws Exception {
        File f = new File(filePath);
        // 파일이 없으면 경로 및 파일 생성
        if (!f.exists()) {
            FileUtil.createFileWithDirectory(filePath);
        }

        FileOutputStream writer = new FileOutputStream(f, append);
        writer.write(data);
        writer.close();

        return true;
    }
}
