package com.github.kangmoo.utils.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {
    public static final String STR_OK = "OK";
    public static final String STR_FAIL = "FAIL";

    private StringUtil() {
    }

    /**
     * boolean을 받아 String형태로 OK, FAIL을 반환
     *
     * @param result
     * @return
     */
    public static String getOkFail(boolean result) {
        return (result ? STR_OK : STR_FAIL);
    }

    /**
     * Sring Formatter. 문자열 format 안의 중괄호 "{}" 를 차례대로 args의 값으로 대치시켜서 반환
     *
     * @param format 문자열 포맷
     * @param args   foramt안의 중괄호 "{}"에 차례대로 대치될 값
     * @return
     */
    public static String format(String format, Object... args) {
        for (Object o : args) {
            format = format.replaceFirst("\\{}", o.toString());
        }
        return format;
    }


    /**
     * 문자열을 받고, 숫자형태이면 true, 그 외 false 반환
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        return (str != null && str.matches("-?\\d+"));
    }

    /**
     * 입력받은 두 문자열의 순위 비교
     *
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

    /**
     * 문자열 에서 "{}" 내의 문자열을 날자에 맞게 변환
     * ex) {yyyyMMDD}Test -> 20200804Test
     *
     * @param in   변환할 문자열
     * @param date 설정할 날
     * @return
     */
    public static String getDateFormString(String in, Date date) {
        String[] splitStr = in.split("\\{");

        for (int i = 0; i < splitStr.length; i++) {
            int endpoint = splitStr[i].lastIndexOf('}');

            if (endpoint < 0 || endpoint == 1) {
                if (endpoint == 1) splitStr[i] = splitStr[i].substring(1);
                continue;
            }

            String dateFormat = splitStr[i].substring(0, endpoint);
            splitStr[i] = new SimpleDateFormat(dateFormat).format(date) + splitStr[i].substring(endpoint + 1);
        }
        StringBuilder sb = new StringBuilder();
        for (String k : splitStr) {

            sb.append(k);
        }

        return sb.toString();
    }

    /**
     * 문자열 에서 "{}" 내의 문자열을 현재 날짜에 맞게 변환
     * ex) {yyyyMMDD}Test -> 20200804Test
     *
     * @param in 변환할 문자열
     * @return
     */
    public static String getDateFormString(String in) {
        return getDateFormString(in, new Date());
    }

    public static String byteArrayToHex(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    public static String byteArrayToHex(byte[] data, String separator) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data)
            sb.append(String.format("%02x", b)).append(separator);
        sb.delete(sb.length() - separator.length(), sb.length());
        return sb.toString();
    }

    private static boolean isEmptyOrNull(String str) {
        if (str != null && !str.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}