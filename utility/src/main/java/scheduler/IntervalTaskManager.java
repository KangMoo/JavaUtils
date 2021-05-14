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
    private static final Logger logger = LoggerFactory.getLogger(IntervalTaskManager.class);
    private static final IntervalTaskManager instance = new IntervalTaskManager();
    private final Map<String, IntervalTaskUnit> jobs = new HashMap<>();
    private ScheduledExecutorService executorService;
    private int defaultInterval = 1000;
    private boolean isStarted = false;

    private IntervalTaskManager() {
    }

    public void init() {
    }

    public static IntervalTaskManager getInstance() {
        return instance;
    }

    public void start() {
        if (isStarted) {
            logger.info("Already Started Interval Task Manager");
            return;
        }
        isStarted = true;
        executorService = Executors.newScheduledThreadPool(jobs.size());
        for (IntervalTaskUnit runner : jobs.values()) {
            executorService.scheduleAtFixedRate(runner,
                    runner.getInterval() - System.currentTimeMillis() % runner.getInterval(),
                    runner.getInterval(),
                    TimeUnit.MILLISECONDS);
        }
        logger.info("Interval Task Manager Start");
    }

    public void stop() {
        if (!isStarted) {
            logger.info("Already Stopped Interval Task Manager");
            return;
        }
        isStarted = false;
        executorService.shutdown();
        logger.info("Interval Task Manager Stop");
    }

    public void addJob(String name, IntervalTaskUnit runner) {
        if (jobs.get(name) != null) {
            logger.warn("() () () Hashmap Key duplication");
            return;
        }
        logger.debug("() () () Add Runner [{}]", name);
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