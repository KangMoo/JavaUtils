package com.github.kangmoo.utils.promiseutil;

import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author kangmoo Heo
 */
@Slf4j
public class PromiseInfo {
    private static final PromiseManager promiseManager = PromiseManager.getInstance();
    @NotBlank
    private final String key;
    private final Runnable onSuccess;
    private final Runnable onFail;
    private final Runnable onTimeout;
    @Min(0)
    private long timeoutMs;
    private final Consumer<Throwable> onError;
    private Map<String, Object> memRepo = new ConcurrentHashMap<>();
    private final AtomicBoolean isDone = new AtomicBoolean(false);


    public PromiseInfo(String key, Runnable onSuccess, Runnable onFail, Runnable onTimeout, long timeoutMs, Consumer<Throwable> onError) {
        this.key = key;
        this.onSuccess = onSuccess;
        this.onFail = onFail;
        this.onTimeout = onTimeout;
        this.timeoutMs = timeoutMs;
        this.onError = onError;
    }

    public void procSuccess() {
        log.debug("[{}] Proc Success Run", key);
        this.runProcess(onSuccess);
    }

    public void procFail() {
        log.debug("[{}] Proc Fail Run", key);
        this.runProcess(onFail);
    }

    public void procTimeout() {
        log.debug("[{}] Proc Timeout Run", key);
        this.runProcess(onTimeout);
    }

    private void runProcess(Runnable runnable) {
        if (!isDone.compareAndSet(false, true)) {
            log.debug("[{}] Already Process done", key);
            return;
        }

        try {
            runnable.run();
            log.debug("[{}] Proc Done Success", key);
        } catch (Exception e) {
            if (onError == null) {
                log.warn("[{}] Err Occurs", key, e);
            } else {
                onError.accept(e);
            }
        } finally {
            promiseManager.removePromiseInfo(key);
        }
    }

    long getTimeoutMs() {
        return timeoutMs;
    }

    public String getKey() {
        return key;
    }

    public void putObject(String key, Object value) {
        this.memRepo.put(key, value);
    }

    public Optional<Object> getObject(String key) {
        return Optional.ofNullable(this.memRepo.get(key));
    }
}