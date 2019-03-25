package com.wffwebdemo.wffwebdemoproject.common.util;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.*;

public enum ScheduledThreadPool {

    NEW_SINGLE_THREAD_SCHEDULED_EXECUTOR(Executors.newSingleThreadScheduledExecutor());

    private static final Map<Runnable, ScheduledFuture<?>> RUNNABLE_SCHEDULED_FUTURE_MAP = new WeakHashMap<>();

    private final ScheduledExecutorService executorService;

    ScheduledThreadPool(ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable,
                                                  long initialDelay,
                                                  long period,
                                                  TimeUnit unit) {

        ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(runnable, initialDelay, period, unit);
        RUNNABLE_SCHEDULED_FUTURE_MAP.put(runnable, scheduledFuture);
        return scheduledFuture;
    }

    public boolean cancel(Runnable runnable, boolean mayInterruptIfRunning) throws IllegalAccessException {
        ScheduledFuture<?> scheduledFuture = RUNNABLE_SCHEDULED_FUTURE_MAP.get(runnable);
        if (scheduledFuture == null) {
            throw new IllegalAccessException("The runnable is not cached");
        }
        boolean cancelled = scheduledFuture.cancel(mayInterruptIfRunning);
        if (cancelled) {
            RUNNABLE_SCHEDULED_FUTURE_MAP.remove(runnable);
        }
        return cancelled;
    }

    public ScheduledFuture<?> getScheduledFuture(Runnable runnable) {
        return RUNNABLE_SCHEDULED_FUTURE_MAP.get(runnable);
    }


}
