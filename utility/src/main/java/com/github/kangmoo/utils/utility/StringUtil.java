package com.github.kangmoo.utils.utility;

import com.google.common.base.CaseFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class StringUtil {
    private static final Random RANDOM = new Random();

    private StringUtil() {
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
        return str == null || str.isEmpty();
    }

    public static String random(int count, boolean letters, boolean numbers) {
        return random(count, 0, 0, letters, numbers);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers) {
        return random(count, start, end, letters, numbers, null, RANDOM);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers, char... chars) {
        return random(count, start, end, letters, numbers, chars, RANDOM);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) {
        if (count == 0) {
            return "";
        } else if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        } else if (chars != null && chars.length == 0) {
            throw new IllegalArgumentException("The chars array must not be empty");
        } else {
            if (start == 0 && end == 0) {
                if (chars != null) {
                    end = chars.length;
                } else if (!letters && !numbers) {
                    end = 1114111;
                } else {
                    end = 123;
                    start = 32;
                }
            } else if (end <= start) {
                throw new IllegalArgumentException("Parameter end (" + end + ") must be greater than start (" + start + ")");
            }

            // int zero_digit_ascii = true;
            // int first_letter_ascii = true;
            if (chars == null && (numbers && end <= 48 || letters && end <= 65)) {
                throw new IllegalArgumentException("Parameter end (" + end + ") must be greater then (" + 48 + ") for generating digits or greater then (" + 65 + ") for generating letters.");
            } else {
                StringBuilder builder = new StringBuilder(count);
                int gap = end - start;

                while (true) {
                    while (count-- != 0) {
                        int codePoint;
                        if (chars == null) {
                            codePoint = random.nextInt(gap) + start;
                            switch (Character.getType(codePoint)) {
                                case 0:
                                case 18:
                                case 19:
                                    ++count;
                                    continue;
                            }
                        } else {
                            codePoint = chars[random.nextInt(gap) + start];
                        }

                        int numberOfChars = Character.charCount(codePoint);
                        if (count == 0 && numberOfChars > 1) {
                            ++count;
                        } else if ((!letters || !Character.isLetter(codePoint)) && (!numbers || !Character.isDigit(codePoint)) && (letters || numbers)) {
                            ++count;
                        } else {
                            builder.appendCodePoint(codePoint);
                            if (numberOfChars == 2) {
                                --count;
                            }
                        }
                    }

                    return builder.toString();
                }
            }
        }
    }

    public static String random(int count, String chars) {
        return chars == null ? random(count, 0, 0, false, false, null, RANDOM) : random(count, chars.toCharArray());
    }

    public static String random(int count, char... chars) {
        return chars == null ? random(count, 0, 0, false, false, null, RANDOM) : random(count, 0, chars.length, false, false, chars, RANDOM);
    }

    public static String camelToSnake(String str) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str);
    }

    public static String snakeToCamel(String str) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
    }
}