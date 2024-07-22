package com.github.kangmoo.utils.common;

import lombok.extern.slf4j.Slf4j;

/**
 * @author kangmoo Heo
 */
@Slf4j
public class SafeExecutor {
    private SafeExecutor() {
    }

    public static void tryRun(ThrowableRunnable r) {
        try {
            r.run();
        } catch (Exception e) {
            log.warn("Err Occurs", e);
        }
    }

    public static void tryRunSilent(ThrowableRunnable r) {
        try {
            r.run();
        } catch (Exception e) {
            // Ignore
        }
    }

    @FunctionalInterface
    public interface ThrowableRunnable {
        void run() throws Exception;
    }

}
