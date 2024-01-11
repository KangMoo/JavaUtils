package com.github.kangmoo.utils;

import org.slf4j.Logger;

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

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

/**
 * @author kangmoo Heo
 */
public class PromiseManager {
    private static final Logger log = getLogger(PromiseManager.class);
    private static final Map<String, PromiseInfo> promiseInfos = new ConcurrentHashMap<>();
    private static ScheduledExecutorService executors = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors(), new BasicThreadFactory.Builder().namingPattern("Promise_Executor-%d").build());

    private PromiseManager() {
    }

    private static class SingletonInstance {
        private static final PromiseManager INSTANCE = new PromiseManager();
    }

    public static PromiseManager getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public static void setThreadCount(int threadCount) {
        if (executors != null) {
            executors.shutdown();
        }
        executors = Executors.newScheduledThreadPool(threadCount, new BasicThreadFactory.Builder().namingPattern("Promise_Executor-%d").build());
    }

    public PromiseInfo createPromiseInfo(String key, Runnable onSuccess, Runnable onFail, Runnable onTimeout, long timeoutMs) {
        return this.createPromiseInfo(key, onSuccess, onFail, onTimeout, timeoutMs, null);
    }

    /**
     * PromiseInfo 생성 및 등록
     *
     * @param key       PromiseInfo의 Key (Unique)
     * @param onSuccess 성공 시 수행될 메서드
     * @param onFail    실패 시 수행될 메서드
     * @param onTimeout Timeout 시 수행될 메서드
     * @param timeoutMs Timeout 까지의 시간 (ms)
     * @param onError   에러 발생 시 수행될 메서드
     * @return 생성된 PromiseInfo
     */
    public PromiseInfo createPromiseInfo(String key, Runnable onSuccess, Runnable onFail, Runnable onTimeout, long timeoutMs, Consumer<Throwable> onError) {
        return registerPromise(key, new PromiseInfo(key, onSuccess, onFail, onTimeout, timeoutMs, onError));
    }

    /**
     * 찾으려는 대상이 없을 경우 10ms마다 확인하면서 최대 100ms까지 대기
     *
     * @param key
     * @return
     */
    public Optional<PromiseInfo> findPromiseInfo(String key) {
        if (key == null) return Optional.empty();
        PromiseInfo promiseInfo = promiseInfos.get(key);
        if (promiseInfo == null) {
            for (int i = 0; i < 10; i++) {
                promiseInfo = promiseInfos.get(key);
                if (promiseInfo != null) break;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return Optional.ofNullable(promiseInfo);
    }

    /**
     * PromiseInfo 내의 Map에 객체 저장
     *
     * @param promiseKey PromiseInfo의 Key
     * @param objectKey  저장할 객체의 Key
     * @param object     저장할 객체
     */
    public void putObject(String promiseKey, String objectKey, Object object) {
        findPromiseInfo(promiseKey).ifPresent(o -> o.putObject(objectKey, object));
    }

    /**
     * PromiseInfo 내의 Map에서 지정된 키가 매핑된 값을 반환한다
     * 이 맵에 키에 대한 매핑이 포함되어 있지 않으면 Optional.empty()를 반환합니다.
     *
     * @param promiseKey PromiseInfo의 Key
     * @param objectKey  반환받을 객체의 Key
     * @return 반환받을 객체
     */
    public Optional<Object> findObject(String promiseKey, String objectKey) {
        return findPromiseInfo(promiseKey).flatMap(o -> o.getObject(objectKey));
    }

    /**
     * 등록된 PromiseInfo를 삭제한다.
     * 삭제된 PromiseInfo는 더 이상 Timeout의 관리 대상이 아니게 된다.
     *
     * @param key PromiseInfo의 Key
     * @return 삭제한 PromiseInfo
     */
    public PromiseInfo removePromiseInfo(String key) {
        return promiseInfos.remove(key);
    }

    /**
     * PromiseInfo의 모든 키 값을 불러온다.
     *
     * @return 관리하고 있는 모든 PromiseInfo의 키
     */
    public Set<String> getPromiseInfoKeys() {
        return new HashSet<>(promiseInfos.keySet());
    }

    private PromiseInfo registerPromise(String key, PromiseInfo promiseInfo) {
        if (promiseInfos.putIfAbsent(key, promiseInfo) != null)
            throw new IllegalArgumentException("Already exist PromiseInfo [" + key + "]");

        executors.schedule(() -> {
            Optional.of(promiseInfos.get(key)).ifPresent(PromiseInfo::procTimeout);
            removePromiseInfo(key);
        }, promiseInfo.getTimeoutMs(), TimeUnit.MILLISECONDS);
        log.debug("Success to add PromiseInfo [{}]", key);
        return promiseInfo;
    }
}
