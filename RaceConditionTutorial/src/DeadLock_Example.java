import java.util.concurrent.atomic.AtomicInteger;

public class DeadLock_Example {
    public static void main(String[] args) {

        // Objective:
        // Shown by "Deadlock.png", thread's want to access both variables in locks
        //    but are not capable of it.

        // We can use standard lock for locks
        //  Lock lock1 = new ReentrantLock();
        //  Lock lock2 = new ReentrantLock();

        // Or objects, with runnable's that interact with them with synchronized
        int count1 = 1;
        int count2 = 2;

        Object lock1 = new Object();
        Object lock2 = new Object();

        System.out.println("count1: " + count1 + " count2: " + count2);

        // DeadLock:
        // Runnable1 locks first Lock 1, and then is not able to Lock 2
        Runnable runnable1 = new Runnable1_Deadlock(count1,count2,lock1,lock2);
        // Runnable2 locks first lock 2, and then not able to lock 1,
        //      because it is waiting for lock 1 to release lock.
        Runnable runnable2 = new Runnable2_Deadlock(count1,count2,lock1,lock2);

        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);

        thread1.start();
        thread2.start();

        try {
            // .join() makes main wait for threads to finnish.
            thread1.join();
            thread2.join();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("count1: " + count1 + " count2: " + count2);

        // Possible fixes:
        // 1. Lock ordering: Make sure that locks dont lock lock's (DeadlockSimple.png)
        //
        //   Fix would be to manually change the code, so that Runnable1_Deadlock and Runnable2_Deadlock
        //       they both lock Lock1 and Lock2 concurrently. (DeadlockSimple_fix.png)
        //   In this case, Thread1 locking Lock1, Thread2 would just wait for Lock1 to release, and then both
        //       would proceed to Lock2.
        //
        // Circular Deadlock:
        // The thing is, we can have Threads that need to use two or three locks, and don't
        //     know how many locks they need. (DeadlockCircular.png)
        // e.g: Thread 1 locks Lock1, but doesn't know that is going to lock Lock2.
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


    }
}
