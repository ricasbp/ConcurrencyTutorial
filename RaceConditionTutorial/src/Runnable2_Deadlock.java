import java.util.concurrent.atomic.AtomicInteger;

public class Runnable2_Deadlock implements Runnable {

    private final AtomicInteger count1;
    private final AtomicInteger count2;

    public Runnable2_Deadlock(AtomicInteger count1, AtomicInteger count2) {
        this.count1 = count1;
        this.count2 = count2;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();

        System.out.println(threadName + ": Trying to lock Count2");
        synchronized (count2) {
            System.out.println(threadName + ": locked Count2");

            // count2 is locked, now we can work with this code.
            count2.incrementAndGet();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(threadName + ": Trying to lock Count1");
            synchronized (count1) {
                System.out.println(threadName + ": locked Count1");
                // We will not be able to enter this code,
                //    since we will be wainting for the other thread to
                //    release the lock on Lock2.
                count1.incrementAndGet();
            }
        }
    }
}
