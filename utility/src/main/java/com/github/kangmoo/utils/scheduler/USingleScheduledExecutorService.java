package com.github.kangmoo.utils.scheduler;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * @author kangmoo Heo
 */
public class USingleScheduledExecutorService {
    private final ScheduledExecutorService scheduler;
    private final BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(1024, true);

    public USingleScheduledExecutorService() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public USingleScheduledExecutorService(ThreadFactory threadFactory) {
        scheduler = Executors.newSingleThreadScheduledExecutor(threadFactory);
    }


//    public void execute(Runnable command) {
//        scheduler.execute(() -> this.execute(command));
//    }

    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return scheduler.schedule(() -> this.execute(command), delay, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return scheduler.scheduleAtFixedRate(() -> this.execute(command), initialDelay, period, unit);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return scheduler.scheduleWithFixedDelay(() -> this.execute(command), initialDelay, delay, unit);
    }

    public void execute(Runnable command) {
        workQueue.add(command);
        Consumer bindedWork = o -> {
        };
        while (true) {
            Runnable r = workQueue.poll();
            if (r == null) break;
            bindedWork = bindedWork.andThen(o -> r.run());
        }
        Consumer finalBindedWork = bindedWork;
        this.scheduler.execute(() -> finalBindedWork.accept(null));
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    public List<Runnable> shutdownNow() {
        return scheduler.shutdownNow();
    }

    public boolean isShutdown() {
        return scheduler.isShutdown();
    }

    public boolean isTerminated() {
        return scheduler.isTerminated();
    }
}
