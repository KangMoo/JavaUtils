package com.github.kangmoo.utils.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author kangmoo Heo
 */
public class UScheduledExecutorService {
    private final USingleScheduledExecutorService[] scheduler;
    private final AtomicInteger counter = new AtomicInteger();
    private final int corePoolSize;

    public UScheduledExecutorService(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        this.scheduler = new USingleScheduledExecutorService[corePoolSize];
        for (int i = 0; i < corePoolSize; i++) {
            scheduler[i] = new USingleScheduledExecutorService();
        }
    }

    public UScheduledExecutorService(int corePoolSize, ThreadFactory threadFactory) {
        this.corePoolSize = corePoolSize;
        this.scheduler = new USingleScheduledExecutorService[corePoolSize];
        for (int i = 0; i < corePoolSize; i++) {
            scheduler[i] = new USingleScheduledExecutorService(threadFactory);
        }
    }

    public void execute(Runnable command) {
        scheduler[getNowPos()].execute(command);
    }

    public void submit(Runnable command) {
        scheduler[getNowPos()].execute(command);
    }

    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return scheduler[getNowPos()].schedule(command, delay, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return scheduler[getNowPos()].scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return scheduler[getNowPos()].scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    private int getNowPos() {
        int nowPos = counter.getAndIncrement();
        if (nowPos >= corePoolSize) {
            nowPos %= corePoolSize;
            counter.set(nowPos);
        }
        return nowPos;
    }


    public void shutdown() {
        for(int i=0; i<corePoolSize; i++){
            scheduler[i].shutdown();
        }
    }

    public List<Runnable> shutdownNow() {
        List<Runnable> list = new ArrayList<>();
        for(int i=0; i<corePoolSize; i++){
            list.addAll(scheduler[i].shutdownNow());
        }
        return list;
    }

    public boolean isShutdown() {
        for(int i=0; i<corePoolSize; i++){
            if(!scheduler[i].isShutdown()) return false;
        }
        return true;
    }

    public boolean isTerminated() {
        for(int i=0; i<corePoolSize; i++){
            if(!scheduler[i].isTerminated()) return false;
        }
        return true;
    }
}
