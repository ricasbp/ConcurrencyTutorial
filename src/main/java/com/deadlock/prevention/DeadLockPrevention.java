package com.deadlock.prevention;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadLockPrevention {
    public static void main(String[] args) {
        // DeadLock Prevention 2: Timeout BackOff
        //     If you try to lock a locked Lock, the thread stops trying,
        //     and it also releases all locks created.

        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();

        int counter1 = 0;
        int counter2 = 0;

        System.out.println("counter1 is " + counter1);
        System.out.println("counter2 is " + counter2);


        // Runnable1 locks first lock1, and then tries lock2
        Runnable runnable1 = new Runnable1Timeout(lock1, lock2, counter1, counter2);

        // Runnable2 tries to lock lock2, and then tries lock1. If not capable, sleeps, and tries again later.
        Runnable runnable2 = new Runnable2Timeout(lock1, lock2, counter1, counter2);

        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("counter1 is " + counter1);
        System.out.println("counter2 is " + counter2);

    }

}
