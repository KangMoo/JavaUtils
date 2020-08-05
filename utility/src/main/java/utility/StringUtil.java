package utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {
    public static final String STR_OK = "OK";
    public static final String STR_FAIL = "FAIL";

    private StringUtil() {
    }

    /**
     * boolean을 받아 String형태로 OK, FAIL을 반환
     * @param result
     * @return
     */
    public static String getOkFail(boolean result) {
        return (result ? STR_OK : STR_FAIL);
    }

    /**
     * Sring Formatter. 문자열 format 안의 중괄호 "{}" 를 차례대로 args의 값으로 대치시켜서 반환
     * @param format 문자열 포맷
     * @param args foramt안의 중괄호 "{}"에 차례대로 대치될 값
     * @return
     */
    public static String stringFomatter(String format, Object ... args){
        for(Object o: args){
            format = format.replaceFirst("\\{}",o.toString());
        }
        return format;
    }


    /**
     * 문자열을 받고, 숫자형태이면 true, 그 외 false 반환
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        return (str != null && str.matches("-?\\d+"));
    }

    /**
     * 입력받은 두 문자열의 순위 비교
     * @param a
     * @param b
     * @return
     */
    public static int strcasecmp(String a, String b) {
        if (a == null || b == null) {
            return 0;
        }

        int alen = a.length();
        int blen = b.length();

        if (alen < blen) {
            return 1;
        } else if (alen > blen) {
            return -1;
        }

        for (int i = 0; i < alen; i++) {
            if (a.charAt(i) < b.charAt(i)) {
                return 1;
            } else if (a.charAt(i) > b.charAt(i)) {
                return -1;
            }
        }

        return 0;
    }

    public static boolean compareString(String a, String b) {
        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return (a.equals(b));
    }

    public static String getDateFormString(String in){
        Date date = new Date();
        String[] splitStr = in.split("\\{");

        for(int i =0; i<splitStr.length;i++){
            int endpoint = splitStr[i].lastIndexOf("}");

            if(endpoint < 0) continue;
            if(endpoint == 1) {splitStr[i] = splitStr[i].substring(1); continue;}

            String dateFormat = splitStr[i].substring(0,endpoint);
            splitStr[i] = new SimpleDateFormat(dateFormat).format(date) + splitStr[i].substring(endpoint+1);
        }
        StringBuilder sb = new StringBuilder();
        for(String k : splitStr){

            sb.append(k);
        }

        return sb.toString();
    }

    public static String getDateFormString(String in, Date date){
        int startIndex = in.indexOf("{");
        int endIndex = in.indexOf("}");
        if(startIndex<0 || endIndex<0) {
            return in;
        }

        String subStr = in.substring(startIndex,endIndex+1);
        SimpleDateFormat sdp = new SimpleDateFormat(subStr);
        String replaceStr = sdp.format(date);
        replaceStr = replaceStr.substring(1,replaceStr.length()-1);
        return in.replace(subStr,replaceStr);
    }



}