package com.deadlock.prevention;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Runnable1Timeout implements Runnable {

    Lock lock1;
    Lock lock2;
    int counter1;
    int counter2;

    public Runnable1Timeout(Lock lock1, Lock lock2, int counter1, int counter2) {
        this.lock1 = lock1;
        this.lock2 = lock2;
        this.counter1 = counter1;
        this.counter2 = counter2;
    }

    // Chatgpt: Any way to make these variables single instances?

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " Started");

        // Trying to lock both of the locks.
        boolean incrementedBothCounters = false;

        while(! incrementedBothCounters){
            int failureCount = 0;
            while(! tryLockBothLocks()){
                failureCount++;
                System.out.println(threadName + " failed to lock both locks");
                System.out.println(threadName + " is waiting before retrying lock." +
                        " [Tried: " + failureCount + " times]");
                sleepForRandomPeriod();
            }

            if (failureCount > 0){
                System.out.println(threadName + " Succeeded on locking both locks after " + failureCount + " times");
                counter1++;
                counter2++;
                System.out.println(threadName + " Incremented both counters");
                incrementedBothCounters = true;

            }

            lock1.unlock();
            lock2.unlock();
        }
    }

    private boolean tryLockBothLocks() {
        String threadName = Thread.currentThread().getName();

        boolean lock1Succeeded;
        boolean lock2Succeeded;

        try {
            lock1Succeeded = lock1.tryLock(1000, TimeUnit.MILLISECONDS);
            if (! lock1Succeeded) {
                return false;
            }

        } catch (InterruptedException e) {
            // In the provided code, the InterruptedException is thrown
            // when the current thread is interrupted while waiting to acquire the lock using the tryLock method with a timeout.
            System.out.println(threadName + " interrupted trying to lock Lock2");
            return false;
        }

        try {
            lock2Succeeded = lock2.tryLock(1000, TimeUnit.MILLISECONDS);
            if (! lock2Succeeded) {
                lock1.unlock();
                return false;
            }
        } catch (InterruptedException e) {
            System.out.println(threadName + " interrupted trying to lock Lock2");
            return false;
        }
            return true;
    }

    private void sleepForRandomPeriod() {
        try {
            // Wait for random amount before trying again
            sleep((long) (1000 * Math.random())) ;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
