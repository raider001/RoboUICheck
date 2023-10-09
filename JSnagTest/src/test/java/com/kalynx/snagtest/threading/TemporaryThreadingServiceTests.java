package com.kalynx.snagtest.threading;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.function.Supplier;

class TemporaryThreadingServiceTests {


    @Test
    void testTimeout() throws InterruptedException {
        AtomicInteger testInt = new AtomicInteger();

        TemporaryThreadingService.schedule(testInt::incrementAndGet)
                .forEvery(Duration.ofMillis(100))
                .over(Duration.ofSeconds(1))
                .andWaitForCompletion();

        Assertions.assertEquals(11, testInt.get());
    }

    @Test
    void testConditionalEnd() throws InterruptedException {
        AtomicInteger testInt = new AtomicInteger();

        Predicate<Integer> conditional = i -> i == 3;

        TemporaryThreadingService.schedule(testInt::incrementAndGet)
                .forEvery(Duration.ofMillis(100))
                .over(Duration.ofSeconds(2))
                .orUntil(conditional)
                .andWaitForCompletion();

        Assertions.assertEquals(3, testInt.get());
    }

    @Test
    void testDurationGoesOverCondition() throws InterruptedException {
        AtomicInteger testInt = new AtomicInteger();

        Predicate<Integer> conditional = i -> i == 30;
        Supplier<Integer> runnable = testInt::incrementAndGet;

        TemporaryThreadingService.schedule(runnable)
                .forEvery(Duration.ofMillis(100))
                .until(conditional)
                .orOver(Duration.ofSeconds(2))
                .andWaitForCompletion();

        Assertions.assertEquals(21, testInt.get());
    }

    @Test
    void testDontWait() {
        AtomicInteger testInt = new AtomicInteger();

        Predicate<Integer> conditional = i -> i == 30;

        TemporaryThreadingService.schedule(testInt::incrementAndGet)
                .forEvery(Duration.ofMillis(100))
                .until(conditional)
                .orOver(Duration.ofSeconds(2))
                .dontWait();
        // This demonstrates non blocking as the above would expect the atomic integer would be greater if it paused and continued.
        // Instead it is '0' as it checked the int immediately after it was fired.
        Assertions.assertEquals(0, testInt.get());
    }
}
