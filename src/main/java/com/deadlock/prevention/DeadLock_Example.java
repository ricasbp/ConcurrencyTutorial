package com.deadlock.prevention;

import org.example.SynchronizedCounter;

import java.util.HashMap;

public class DeadLock_Example {

    // This code contains a example of:
    // - DeadLock:
    //         explained better with actually counters (Critical Sections) to manipulate.
    // - DeadLock prevention:
    //          Lock Reordering

    public static void main(String[] args) {

        // Objective:
        // Shown by "DeadlockSimple.png", thread's want to access two variables in two separate locks
        //    but are not capable of it.

        // We can use standard lock for locks
        //  Lock lock1 = new ReentrantLock();
        //  Lock lock2 = new ReentrantLock();

        // Or objects, with runnable's that interact with them with synchronized
        Object lock1 = new Object();
        Object lock2 = new Object();

        SynchronizedCounter syncCount1 = new SynchronizedCounter(0);
        SynchronizedCounter syncCount2 = new SynchronizedCounter(0);

        System.out.println("count1: " + syncCount1.getCount() + " count2: " + syncCount2.getCount());

        // DeadLock: (DeadlockSimple.png)
        // Runnable1 locks first Lock 1, and then is not able to Lock 2
        // Runnable2 locks first lock 2, and then not able to lock 1,
        //      because it is waiting for lock 1 to release lock.

        // Fix Deadlock by using the same Runnable for both Threads. (Lock Reordering)
        Thread thread1 = new Thread(getRunnable1(syncCount1, syncCount2, lock1, lock2));
        Thread thread2 = new Thread(getRunnable2(syncCount1, syncCount2, lock1, lock2));

        thread1.start();
        thread2.start();

        try {
            // .join() makes main wait for threads to finish.
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("count1: " + syncCount1.getCount() + " count2: " + syncCount2.getCount());
        // Notes:
        /*
            // Possible fixes:
            // 2. Timeout backoff (Next Tutorial)
            // 3. Deadlock Detection (Next Tutorial)
            //
            // 1. Lock Reordering: Make sure that locks dont lock both lock's. (DeadlockSimple_fix.png)
            //
            //   Fix would be to manually change the code, so that Runnable1_Deadlock and Runnable2_Deadlock
            //       they both lock Lock1 and Lock2 concurrently.
            //   In this case, Thread1 locking Lock1, Thread2 would just wait for Lock1 to release, and then both
            //       would proceed to Lock2.
            //
            // Circular Deadlock:
            // The thing is, we can have Threads that need to use two or three locks, and don't
            //     know how many locks they need. (DeadlockCircular.png)
            // e.g: Thread 1 locks Lock1, but doesn't know what is locking Lock2.
            //
            // Note: (DeadlockComplex.png)
            // Deadlocks will always block any other extra threads that try to access deadlocks that are happening.
            //
            // Conditions to guarantee DeadLocks:
            // 1. Mutual Exclusion:
            //      Only one thread can access each resource.
            // 2. No Preemption:
            //      One lock cannot open up for others to access it.
            // 3. Hold and Wait:
            //      Holds a lock and waits to obtain other locks.
            //      If it didn't wait forever, deadlock would not happen.
            // 4. Circular Wait:
            //      We will need to have a circular graph.
            //      If thread1 didn't have access to the lock1, and create the circular pattern,
            //      we wouldn't have a deadlock situation.
            //
            // Other types of phenomenons that cause threads to block:
            // LiveLock: One or more threads are trying to resolve the problem, but try to resolve it infinitely.
            // Nested Monitor Lockout: (?)
            // Reentrance Lockout: (?)
            // Starvation: Thread with low priority is always at the end of queue waiting to enter.
         */
    }

    private static Runnable getRunnable1(SynchronizedCounter syncCount1, SynchronizedCounter syncCount2, Object lock1, Object lock2) {
        return () -> {

            String threadName = Thread.currentThread().getName();

            System.out.println(threadName + ": Trying to lock Lock1 for Count1");
            synchronized (lock1){
                System.out.println(threadName + ": locked Count1");
                syncCount1.increment();

                // Give enough time for the other thread to lock the other Lock.
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println(threadName + ": Trying to lock Lock2 for Count2");
                synchronized (lock2){
                    // We will not be able to enter this code,
                    //    since we will be waiting for the other thread to
                    //    release the lock on Lock2.

                    System.out.println(threadName + ": locked Count2");
                    syncCount2.increment();
                }
            }
        };
    }

    private static Runnable getRunnable2(SynchronizedCounter syncCount1, SynchronizedCounter syncCount2, Object lock1, Object lock2) {
        return () -> {

            String threadName = Thread.currentThread().getName();

            System.out.println(threadName + ": Trying to lock Lock2 for Count2");
            synchronized (lock2){
                System.out.println(threadName + ": locked Count2");
                syncCount2.increment();

                //Give enough time for the other thread to lock the other Lock.
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


                System.out.println(threadName + ": Trying to lock Lock1 for Count1 ");
                synchronized (lock1){
                    // We will not be able to enter this code,
                    //    since we will be waiting for the other thread to
                    //    release the lock on Lock1.

                    System.out.println(threadName + ": locked Count1");
                    syncCount1.increment();
                }
            }
        };
    }
}
