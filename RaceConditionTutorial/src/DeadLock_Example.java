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
        AtomicInteger count1 = new AtomicInteger(1);
        AtomicInteger count2 = new AtomicInteger(2);

        // DeadLock:
        // Runnable 1 locks first Lock 1, and then is not able to Lock 2
        Runnable runnable1 = new Runnable1_Deadlock(count1,count2);
        // Runnable 2 locks first lock 2, and then not able to lock 1,
        //      because it is waiting for lock 1 to release lock.
        Runnable runnable2 = new Runnable2_Deadlock(count1,count2);

        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);

        thread1.start();
        thread2.start();
    }
}
