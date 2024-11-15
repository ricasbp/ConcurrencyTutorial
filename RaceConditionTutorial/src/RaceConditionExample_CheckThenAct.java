

public class RaceConditionExample_CheckThenAct {
    public static void main(String[] args) {

        SynchronizedCounter synchronizedCounter = new SynchronizedCounter();

        // Map<String, String> sharedMap = new ConcurrentHashMap<>();

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