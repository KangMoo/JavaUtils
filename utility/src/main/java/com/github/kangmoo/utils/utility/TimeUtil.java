package com.github.kangmoo.utils.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author kangmoo Heo
 */
public class TimeUtil {
    private TimeUtil() {
    }

    public static void trySleep(int msec) {
        try {
            Thread.sleep(msec);
        } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
        }
    }

    public static long dateFormat(String time, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(time).getTime();
    }

    public static String dateFormat(long time, String format) {
        return new SimpleDateFormat(format).format(new Date(time));
    }
}
