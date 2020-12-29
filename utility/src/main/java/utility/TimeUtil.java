package utility;

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
}
