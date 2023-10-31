package com.github.kangmoo.utils.common;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author kangmoo Heo
 */
public class RoundRobinCounter {
    private final AtomicInteger counter = new AtomicInteger(0);
    private final int start;
    private final int end;

    public RoundRobinCounter(int start, int end) {
        this.start = start;
        this.end = end;
        this.counter.set(start);
    }

    public synchronized int getNext() {
        int count = counter.incrementAndGet();
        if (count >= end) {
            counter.set(start);
            count = start;
        }
        return count;
    }
}