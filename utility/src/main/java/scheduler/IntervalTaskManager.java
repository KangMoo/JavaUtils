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
 *
 * @author Kangmoo Heo
 */
public class IntervalTaskManager {
    private static final Logger log = LoggerFactory.getLogger(IntervalTaskManager.class);
    private static IntervalTaskManager instance = new IntervalTaskManager();
    private final Map<String, IntervalTaskUnit> jobs = new HashMap<>();
    private ScheduledExecutorService executorService;
    private int defaultInterval = 1000;

    private IntervalTaskManager() {
    }

    public void init() {
    }

    public static IntervalTaskManager getInstance() {
        if (instance == null) {
            instance = new IntervalTaskManager();
        }
        return instance;
    }

    public void start() {
        executorService = Executors.newScheduledThreadPool(jobs.size());
        for (IntervalTaskUnit runner : jobs.values()) {
            executorService.scheduleAtFixedRate(runner,
                    runner.getInterval() - System.currentTimeMillis() % runner.getInterval(),
                    runner.getInterval(),
                    TimeUnit.MILLISECONDS);
        }
        log.debug("() () () Timeout Msg Handler Start");
    }

    public void stop() {
        executorService.shutdown();
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
        for (IntervalTaskUnit runner : jobs.values()) {
            runner.run();
        }
    }

    public int getDefaultInterval() {
        return defaultInterval;
    }

    public void setDefaultInterval(int msec) {
        this.defaultInterval = msec;
    }
}