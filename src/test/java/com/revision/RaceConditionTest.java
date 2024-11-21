package com.revision;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class RaceConditionTest {

    SynchronizedCounter counter;
    int incrementCount;

    Thread t1;
    Thread t2;
    Thread t3;
    Thread t4;

    Runnable runnableIncrementCounter;

    @BeforeEach
    void setUp() {
        counter  = new SynchronizedCounter(0);
        incrementCount = 5;

        runnableIncrementCounter = RaceConditionRunnable.getRunnable(counter, incrementCount);
        this.t1 = new Thread(runnableIncrementCounter);
        this.t2 = new Thread(runnableIncrementCounter);
        this.t3 = new Thread(runnableIncrementCounter);
        this.t4 = new Thread(runnableIncrementCounter);
    }

    @Test
    void twoThreadsTest() {
        int numberOfThreads = 2;

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int expectingResult = (incrementCount * (2 * numberOfThreads)) - 1;

        Assertions.assertEquals(counter.getCounter(), expectingResult);
    }

    @Test
    void threeThreadsTest() {
        int numberOfThreads = 3;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(runnableIncrementCounter);
        }

        executorService.shutdown();

        // Wait for all tasks to complete
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                throw new RuntimeException("Executor did not terminate in the specified time.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int expectingResult = (incrementCount * (2 * numberOfThreads)) - 1;

        Assertions.assertEquals(counter.getCounter(), expectingResult);

    }

    @Test
    void fourThreadsTest() {
        int numberOfThreads = 4;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(runnableIncrementCounter);
        }

        executorService.shutdown();

        // Wait for all tasks to complete
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                throw new RuntimeException("Executor did not terminate in the specified time.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        int expectingResult = (incrementCount * (2 * numberOfThreads)) - 1;
        Assertions.assertEquals(counter.getCounter(), expectingResult);
    }

    @Test
    void fiveThreadsTest() {
        int numberOfThreads = 5;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(runnableIncrementCounter);
        }

        executorService.shutdown();

        // Wait for all tasks to complete
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                throw new RuntimeException("Executor did not terminate in the specified time.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        int expectingResult = (incrementCount * (2 * numberOfThreads)) - 1;
        Assertions.assertEquals(counter.getCounter(), expectingResult);
    }
}