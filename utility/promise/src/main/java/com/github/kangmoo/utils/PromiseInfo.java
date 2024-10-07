package com.github.kangmoo.utils;

import lombok.Getter;
import lombok.NonNull;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

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
    @Getter
    private final String key;
    private final Runnable onSuccess;
    private final Runnable onFail;
    private final Runnable onTimeout;
    @Getter
    private final long timeoutMs;
    private final Consumer<Throwable> onError;
    private final AtomicBoolean isDone = new AtomicBoolean(false);
    private Map<String, Object> memRepo;

    public static PromiseInfoBuilder newBuilder() {
        return new PromiseInfoBuilder();
    }

    public PromiseInfo(@NonNull String key, Runnable onSuccess, Runnable onFail, Runnable onTimeout, long timeoutMs, Consumer<Throwable> onError) {
        if (timeoutMs <= 0) {
            throw new IllegalArgumentException("TimeoutMs must be greater than 0");
        }

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

    public void cancel() {
        if (!isDone.compareAndSet(false, true)) {
            log.debug("[{}] Already Process done", key);
            return;
        }
        promiseManager.removePromiseInfo(key);
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

    @Synchronized
    public void putObject(String key, Object value) {
        if (memRepo == null) {
            memRepo = new ConcurrentHashMap<>();
        }
        this.memRepo.put(key, value);
    }

    @Synchronized
    public Optional<Object> getObject(String key) {
        if (memRepo == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.memRepo.get(key));
    }
}