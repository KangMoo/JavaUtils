package com.github.kangmoo.utils.scheduler;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Kangmoo Heo
 */
@Slf4j
public class IntervalTaskManager {

    private static final List<IntervalTaskUnit> itu = List.of(
            // new SessionMonitor()
    );

    private IntervalTaskManager() {
    }

    public static void startAll() {
        itu.forEach(IntervalTaskUnit::start);
    }

    public static void stopAll() {
        itu.forEach(IntervalTaskUnit::stop);
    }
}