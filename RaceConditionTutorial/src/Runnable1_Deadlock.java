import java.util.concurrent.atomic.AtomicInteger;

public class Runnable1_Deadlock implements Runnable {

    private final AtomicInteger count1;
    private final AtomicInteger count2;

    public Runnable1_Deadlock(AtomicInteger count1, AtomicInteger count2) {
        this.count1 = count1;
        this.count2 = count2;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();

        System.out.println(threadName + ": Trying to lock Lock 1");
        synchronized (count1) {
            System.out.println(threadName + ": locked Lock 1");

            // Lock 1 is locked, now we can work with it.
            count1.incrementAndGet();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(threadName + ": Trying to lock Lock 2");
            synchronized (count2) {
                System.out.println(threadName + ": locked Lock 2");
                // We will not be able to enter this code,
                //    since we will be waiting for the other thread to
                //    release the lock on Lock2.
                count2.incrementAndGet();
            }
        }
    }
}
