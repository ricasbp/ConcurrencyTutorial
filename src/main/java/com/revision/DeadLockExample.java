package com.revision;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadLockExample {
    public static void main(String[] args) {

        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();

        AtomicInteger counter1 = new AtomicInteger(0);
        AtomicInteger counter2 = new AtomicInteger(0);

        System.out.println(counter1.get());
        System.out.println(counter2.get());


        Runnable runnable1 = () -> {
            String threadName = Thread.currentThread().getName();
            boolean lockedBothLocks = false;

            while(!lockedBothLocks) {
                System.out.println(threadName + " is Locking lock1: " + lock1.toString());
                sleepThread();
                boolean lock1Succeeded = lock1.tryLock();

                System.out.println(threadName + " is Locking lock2: " + lock2.toString());
                sleepThread();
                boolean lock2Succeeded = lock2.tryLock();

                if(lock1Succeeded && lock2Succeeded) {
                    // We now have both locks
                    System.out.println(threadName + " Succeeded on locking both locks after");

                    counter1.incrementAndGet();
                    counter2.incrementAndGet();
                    lockedBothLocks = true;
                }else{
                    lock1.unlock();
                }


            }
            lock1.unlock();
            lock2.unlock();
        };

        Runnable runnable2 = () -> {
            String threadName = Thread.currentThread().getName();
            boolean lockedBothLocks = false;

            while(!lockedBothLocks) {
                System.out.println(threadName + " is Locking lock1: " + lock2.toString());
                sleepThread();
                boolean lock2Succeeded = lock2.tryLock();

                System.out.println(threadName + " is Locking lock2: " + lock1.toString());
                sleepThread();
                boolean lock1Succeeded = lock1.tryLock();

                if(lock1Succeeded && lock2Succeeded) {
                    // We now have both locks
                    System.out.println(threadName + " Succeeded on locking both locks after");

                    counter1.incrementAndGet();
                    counter2.incrementAndGet();
                    lockedBothLocks = true;
                }else{
                    lock2.unlock();
                }


            }
            lock1.unlock();
            lock2.unlock();
        };


        Thread t1 = new Thread(runnable1);
        Thread t2 = new Thread(runnable2);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(counter1.get());
        System.out.println(counter2.get());

    }

    private static void sleepThread() {
        try {
            Thread.sleep(1000); // Wait 1 sec
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
