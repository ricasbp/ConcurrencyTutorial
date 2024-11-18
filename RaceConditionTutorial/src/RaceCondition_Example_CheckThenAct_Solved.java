import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RaceCondition_Example_CheckThenAct_Solved {
    public static void main(String[] args) {

        /*
            This hashmaps allows for multiple threads to access the values concurrently,
             without becoming internally inconsistent.
            ConcurrentHashMap is thread-safe and prevents data corruption,
            but it doesn't implement ACID properties,
            as they apply to transactions in databases.
         */
        Map<String, String> sharedMap = new ConcurrentHashMap<>();

        Thread thread1 = new Thread(getRunnable(sharedMap)); // Same runnable, but no same instance.
        Thread thread2 = new Thread(getRunnable(sharedMap));

        thread1.start();
        thread2.start();

    }

    private static Runnable getRunnable(Map<String, String> sharedMap) {
        return () -> {
            for(int i  = 0; i < 1_000_000; i++) {
                // With a synchronized block, only on thread can execute the 'if' check.
                synchronized (sharedMap){
                    if(sharedMap.containsKey("key")){
                        String value = sharedMap.remove("key");
                        if(value == null){
                            System.out.println("For iteration: i = " + i + ": We were expecting a value for key, " +
                                    "but it waS null");
                        }
                    }else{
                        sharedMap.put("key", "valueABC");
                    }
                }
            }
        };
    }
    /*
        Note: ConcurrentHashMap's are not enough to solve this concurrent problem because,
        When you perform compound actions, such as containsKey() followed by remove(), these actions are not atomic.
        Another thread could modify the map between the containsKey() check and the subsequent remove() call.

        Note2: Atomic doesn't mean the same atomicity from ACID.
               Atomic means:
               The check (containsKey()) and the action (remove()) are not performed as a single, indivisible operation.
     */
}