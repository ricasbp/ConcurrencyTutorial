import java.util.concurrent.atomic.AtomicInteger;

public class RaceCondition_Example_ReadModifyWrite2_Solved {
    public static void main(String[] args) {

        /*
            Instead of using SynchronizedCounter, we could use an AtomicInteger.
            AtomicInteger implements Thread-safety with lower overhead because of CAS (Compare and Swap).
            CAS (Compare and Swap) allows for synchronization mechanism without using locks.
            This make is more efficient than Synchronized blocks.
         */
        AtomicInteger atomicCounter = new AtomicInteger(0);

        // Two threads simulating concurrent access
        Thread thread1 = new Thread(getRunnable(atomicCounter, "Thread1 final count: "));
        Thread thread2 = new Thread(getRunnable(atomicCounter, "Thread2 final count: "));

        thread1.start();
        thread2.start();
    }

    private static Runnable getRunnable(AtomicInteger atomicCounter, String message) {
        return () -> {
            for (int i = 0; i < 1_000_000; i++) {
                atomicCounter.incrementAndGet();  // Atomically increments the counter
            }
            System.out.println(message + atomicCounter.get());
        };
    }
}