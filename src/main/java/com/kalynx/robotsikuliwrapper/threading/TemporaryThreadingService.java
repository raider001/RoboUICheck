package com.kalynx.robotsikuliwrapper.threading;


import org.apache.commons.lang3.NotImplementedException;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class TemporaryThreadingService<T> {
    private final Duration threadTimeoutDuration = Duration.ofSeconds(60);
    private Supplier<T> runnable;
    private Duration forEvery;
    private Duration forPeriod;

    private Function<T, Boolean> until = val -> false;

    // Thread used to handle the timing and delegation to the worker thread.
    private final ScheduledExecutorService timerService = Executors.newSingleThreadScheduledExecutor();
    // The actual worker thread pool used for handling process.
    private final ExecutorService workerService = Executors.newCachedThreadPool();
    // The timeout thread used to cancel all other threads once the time is met.
    private final ScheduledExecutorService completionService = Executors.newSingleThreadScheduledExecutor();

    // Hide CTOR
    private TemporaryThreadingService() {

    }
    public static <T> Runner schedule(Supplier<T> runnable) {

        TemporaryThreadingService<T> tempThreadingService = new TemporaryThreadingService<>();
        return new Runner<>(tempThreadingService, runnable);
    }

    public static class Runner<T> {
        private final TemporaryThreadingService<T> service;
        private Runner(TemporaryThreadingService<T> service, Supplier<T> runnable) {
            this.service = service;
            service.runnable = runnable;
        }

        public ForEvery<T> forEvery(Duration duration) {
            return new ForEvery<>(service, duration);
        }
    }

    public static class ForEvery<T> {
        private final TemporaryThreadingService<T> service;

        ForEvery(TemporaryThreadingService<T> service, Duration duration) {
            this.service = service;
            service.forEvery = duration;
        }

        public Until<T> over(Duration period) {
           return new Until<>(service, period);
        }

        public Until<T> until(Function<T, Boolean> until) {
            return new Until<>(service, until);
        }
    }

    public static class Until<T> {
        private final TemporaryThreadingService<T> service;
        private Until(TemporaryThreadingService<T> service, Duration duration) {
            this.service = service;
            service.forPeriod = duration;
        }

        private Until(TemporaryThreadingService<T> service, Function<T, Boolean> until) {
            this.service = service;
            service.until = until;
        }

        public Until<T> orOver(Duration period) {
            return new Until<>(service, period);
        }

        public Until<T> orUntil(Function<T, Boolean> until) {
            return new Until<>(service, until);
        }

        public void andWaitForCompletion() throws InterruptedException {
            handleThread();
            service.workerService.awaitTermination(service.threadTimeoutDuration.toMillis() + service.forPeriod.toMillis(), TimeUnit.MILLISECONDS);
            service.completionService.shutdown();
        }

        public void dontWait() {
            handleThread();
        }

        private void handleThread() {

            ThreadingServiceRunnable<T> serviceRunnable = new ThreadingServiceRunnable<>(service.runnable,service.until, service);
            Future<?> future = service.timerService.scheduleAtFixedRate(serviceRunnable,
                    0,
                    service.forEvery.toMillis(),
                    TimeUnit.MILLISECONDS);
            // Really strange and could lead to strange behaviour..
            serviceRunnable.setFuture(future);

            service.completionService.schedule(() ->{
                future.cancel(false);
                service.workerService.shutdown();
                service.timerService.shutdown();
            }, service.forPeriod.toMillis(), TimeUnit.MILLISECONDS);
        }
    }

    private static class ThreadingServiceRunnable<T> implements Runnable {
        private final Supplier<T> runnableSupplier;
        private final Function<T, Boolean> conditionalCheck;
        private final TemporaryThreadingService<T> service;
        private Future<?> future;
        public ThreadingServiceRunnable(Supplier<T> runnableSupplier, Function<T,Boolean> conditionalCheck, TemporaryThreadingService<T> service)  {
            this.runnableSupplier = runnableSupplier;
            this.conditionalCheck = conditionalCheck;
            this.service = service;
        }

        public void setFuture(Future<?> future) {
            this.future = future;
        }

        @Override
        public void run() {
            Boolean res = conditionalCheck.apply(runnableSupplier.get());
            if (res == null || !res) {
                return;
            }
            cancel();
        }

        void cancel() {
            future.cancel(false);
            service.workerService.shutdown();
            service.timerService.shutdown();
        }
    }
}
