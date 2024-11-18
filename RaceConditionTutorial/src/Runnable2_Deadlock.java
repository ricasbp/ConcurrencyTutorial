import java.util.concurrent.atomic.AtomicInteger;

public class Runnable2_Deadlock implements Runnable {

    private int count1;
    private int count2;
    private final Object lock1;
    private final Object lock2;

    public Runnable2_Deadlock(int count1, int count2, Object lock1, Object lock2) {
        this.count1 = count1;
        this.count2 = count2;
        this.lock1 = lock1;
        this.lock2 = lock2;
    }

    @Override
    public void run()  {
        String threadName = Thread.currentThread().getName();

        System.out.println(threadName + ": Trying to lock Count2");
        synchronized (lock2) {
            System.out.println(threadName + ": locked Count2");

            // count2 is locked, now we can work with this code.
            count2++;

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(threadName + ": Trying to lock Count1");
            synchronized (lock1) {
                System.out.println(threadName + ": locked Count1");
                // We will not be able to enter this code,
                //    since we will be wainting for the other thread to
                //    release the lock on Lock2.
                count1++;
            }
        }
    }
}
