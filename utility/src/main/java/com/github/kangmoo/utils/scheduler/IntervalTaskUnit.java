package com.github.kangmoo.utils.scheduler;

/**
 * @author kangmoo Heo
 */
public abstract class IntervalTaskUnit implements Runnable {
    protected int interval;

    public IntervalTaskUnit(int interval) {
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
