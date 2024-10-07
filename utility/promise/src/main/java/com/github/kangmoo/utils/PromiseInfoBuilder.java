package com.github.kangmoo.utils;

import lombok.Getter;
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
public class PromiseInfoBuilder {
    private String key;
    private Runnable onSuccess;
    private Runnable onFail;
    private Runnable onTimeout;
    private long timeoutMs;
    private Consumer<Throwable> onError;

    PromiseInfoBuilder() {
    }

    public PromiseInfoBuilder setKey(String key) {
        this.key = key;
        return this;
    }

    public PromiseInfoBuilder setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public PromiseInfoBuilder setOnFail(Runnable onFail) {
        this.onFail = onFail;
        return this;
    }

    public PromiseInfoBuilder setOnTimeout(Runnable onTimeout) {
        this.onTimeout = onTimeout;
        return this;
    }

    public PromiseInfoBuilder setTimeoutMs(long timeoutMs) {
        this.timeoutMs = timeoutMs;
        return this;
    }

    public PromiseInfoBuilder setOnError(Consumer<Throwable> onError) {
        this.onError = onError;
        return this;
    }

    public PromiseInfo build() {
        return new PromiseInfo(key, onSuccess, onFail, onTimeout, timeoutMs, onError);
    }
}