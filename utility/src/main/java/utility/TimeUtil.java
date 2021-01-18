package utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author kangmoo Heo
 */
public class TimeUtil {
    private TimeUtil() {
    }

    public static void trySleep(int msec) {
        try {
            Thread.sleep((long)msec);
        } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
        }
    }

    public static long changeDateFormat(String time, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(time).getTime();
    }
}
