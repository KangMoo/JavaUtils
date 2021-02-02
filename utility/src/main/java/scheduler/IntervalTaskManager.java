package scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Session Manager
 * 주기적으로 세션을 필터링하고 처리 (Default 1초)
 * @author Kangmoo Heo
 */
public class IntervalTaskManager {

    private static final Logger log = LoggerFactory.getLogger(IntervalTaskManager.class);

    // IntervalTaskManager 싱글턴 변수
    private static IntervalTaskManager instance;
    // Interval Task 목록
    private final Map<String, IntervalTaskUnit> jobs = new HashMap<>();
    // Interval Tasker (쓰레드) 목록
    private final Set<ScheduledExecutorService> runners = new HashSet<>();
    // Interval Time (milli sec)
    private int defaultInterval = 1000;

    private IntervalTaskManager() {
    }

    public static IntervalTaskManager getInstance() {
        if (instance == null) {
            instance = new IntervalTaskManager();
        }
        return instance;
    }

    public void start() {
        for (IntervalTaskUnit runner : jobs.values()) {
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            runners.add(executorService);
            executorService.scheduleAtFixedRate(runner,
                    runner.getInterval() - System.currentTimeMillis() % runner.getInterval(),
                    runner.getInterval(),
                    TimeUnit.MILLISECONDS);
        }
        log.debug("() () () Timeout Msg Handler Start");
    }

    public void stop() {
        for (ScheduledExecutorService executorService : runners) {
            try {
                executorService.shutdown();
            } catch (Exception e) {
                log.warn("Fail to Shutdown Interval Time Job");
            }
        }
        runners.clear();
        log.debug("() () () Session Manager Stop");
    }

    public void addJob(String name, IntervalTaskUnit runner) {
        if (jobs.get(name) != null) {
            log.warn("() () () Hashmap Key duplication");
            return;
        }

        log.debug("() () () Add Runner [{}]", name);
        jobs.put(name, runner);
    }

    public void sessionCheck() {
        for (Runnable runner : jobs.values()) {
            runner.run();
        }
    }

    public int getDefaultInterval() {
        return defaultInterval;
    }

    public void setDefaultInterval(int mSec) {
        this.defaultInterval = mSec;
    }
}
