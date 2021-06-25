package utility;

import java.util.concurrent.*;

/**
 *
 * @author kangmoo Heo
 */
public class SimpleExecutor {
    public static Future<?> execute(Runnable runnable) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            return executorService.submit(runnable);
        } finally {
            executorService.shutdown();
        }
    }

    public static <T> Future<T> execute(Callable<T> task) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            return executorService.submit(task);
        } finally {
            executorService.shutdown();
        }
    }
}
