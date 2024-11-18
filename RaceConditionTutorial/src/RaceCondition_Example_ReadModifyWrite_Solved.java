

public class RaceCondition_Example_ReadModifyWrite_Solved {
    public static void main(String[] args) {
        // Objective:
        // Increment a Counter with 2 threads.

        SynchronizedCounter synchronizedCounter = new SynchronizedCounter();
        // If we didn't have a Counter that was synchronized,
        //     aka, it didn't provide thread-safe access,
        //     the program would not execute has expected.
        //  (InterLeavedAccess.png) happens if we don't have locks.
        //  (SequentialAccess.png) happens if we have locks.

        Thread thread1 = new Thread( getRunnable(synchronizedCounter, "Thread1 final count: "));
        Thread thread2 = new Thread( getRunnable(synchronizedCounter, "Thread2 final count: "));
        thread1.start();
        thread2.start();

    }

    private static Runnable getRunnable(SynchronizedCounter synchronizedCounter, String message) {
        return () -> {
            for (int i = 0; i < 1_000_000; i++) {
                synchronizedCounter.increment();
            }
            System.out.println(message + synchronizedCounter.getCount());
        };
        // This is a shorthand way of creating an anonymous inner class that implements Runnable,
        //   without having to write out new Runnable() { public void run() { ... } }.
    }
}