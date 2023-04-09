package com.github.kangmoo.utils.scheduler;


import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author kangmoo Heo
 */
@Slf4j
public abstract class IntervalTaskUnit implements Runnable {
    protected static final Set<String> RUNNING_SCHEDULERS = new ConcurrentSkipListSet<>();
    protected String id = "ITV_" + this.getClass().getSimpleName();
    protected ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, id));
    protected int interval;

    protected IntervalTaskUnit(int interval) {
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void start() {
        if (!RUNNING_SCHEDULERS.add(id)) {
            log.warn("Interval Task Job Already Started {}", id);
            return;
        }

        scheduledExecutorService.scheduleAtFixedRate(this::proc,
                this.interval - System.currentTimeMillis() % this.interval,
                this.interval, TimeUnit.MILLISECONDS);
        log.info("Interval Task Job Start {}", id);
    }

    public void stop() {
        if (!RUNNING_SCHEDULERS.remove(id)) {
            log.warn("Interval Task Job Already Stopped {}", id);
            return;
        }

        scheduledExecutorService.shutdown();
        log.info("Interval Task Job Stop {}", id);
    }

    private void proc() {
        try {
            run();
        } catch (Exception e) {
            log.warn("Err Occurs", e);
        }
    }
}
