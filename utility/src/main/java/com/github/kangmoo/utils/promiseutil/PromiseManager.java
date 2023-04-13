package com.github.kangmoo.utils.promiseutil;

import com.github.kangmoo.utils.utility.TimeUtil;
import com.github.kangmoo.utils.utility.ValidationUtil;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;

import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author kangmoo Heo
 */
public class PromiseManager {
    private static final Logger log = getLogger(PromiseManager.class);
    private static final PromiseManager INSTANCE = new PromiseManager();
    private static final Map<String, PromiseInfo> promiseInfos = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService executors = Executors.newScheduledThreadPool(10, new BasicThreadFactory.Builder().namingPattern("Promise_Executor-%d").build());

    private PromiseManager() {
    }

    public static PromiseManager getInstance() {
        return INSTANCE;
    }

    public PromiseInfo createPromiseInfo(String key, Runnable onSuccess, Runnable onFail, Runnable onTimeout, long timeoutMs) {
        return this.createPromiseInfo(key, onSuccess, onFail, onTimeout, timeoutMs, null);
    }

    public PromiseInfo createPromiseInfo(String key, Runnable onSuccess, Runnable onFail, Runnable onTimeout, long timeoutMs, Consumer<Throwable> onError) {
        return registerPromise(key, new PromiseInfo(key, onSuccess, onFail, onTimeout, timeoutMs, onError));
    }

    public Optional<PromiseInfo> findPromiseInfo(String key) {
        if (key == null) return Optional.empty();
        PromiseInfo promiseInfo = promiseInfos.get(key);
        if (promiseInfo == null) {
            for (int i = 0; i < 10; i++) {
                promiseInfo = promiseInfos.get(key);
                if (promiseInfo != null) break;
                TimeUtil.trySleep(10);
            }
        }
        return Optional.ofNullable(promiseInfo);
    }

    public void putObject(String promiseKey, String objectKey, Object object) {
        findPromiseInfo(promiseKey).ifPresent(o -> o.putObject(objectKey, object));
    }

    public Optional<Object> findObject(String promiseKey, String objectKey) {
        return findPromiseInfo(promiseKey).flatMap(o -> o.getObject(objectKey));
    }

    public PromiseInfo removePromiseInfo(String key) {
        return promiseInfos.remove(key);
    }

    public Set<String> getPromiseInfoKeys() {
        return new HashSet<>(promiseInfos.keySet());
    }

    private PromiseInfo registerPromise(String key, PromiseInfo promiseInfo) {
        ValidationUtil.validCheck(promiseInfo);
        if (promiseInfos.putIfAbsent(key, promiseInfo) != null)
            throw new ValidationException("Already exist PromiseInfo [" + key + "]");

        executors.schedule(() -> {
            findPromiseInfo(key).ifPresent(PromiseInfo::procTimeout);
            removePromiseInfo(key);
        }, promiseInfo.getTimeoutMs(), TimeUnit.MILLISECONDS);
        log.debug("Success to add PromiseInfo [{}]", key);
        return promiseInfo;
    }
}
