package org.example;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RaceCondition_Example_CheckThenAct {
    public static void main(String[] args) {

        /*
            This hashmaps allows for multiple threads to access the values concurrently, without becoming internally inconsistent.
            ConcurrentHashMap is thread-safe and prevents data corruption,
            but it doesn't implement ACID properties, as they apply to transactions in databases.
         */
        Map<String, String> sharedMap = new ConcurrentHashMap<>();


        Thread thread1 = new Thread(getRunnable(sharedMap));
        // Same runnable, but another instance.
        Thread thread2 = new Thread(getRunnable(sharedMap));

        thread1.start();
        thread2.start();

    }

    private static Runnable getRunnable(Map<String, String> sharedMap) {
        return () -> {
            for(int i  = 0; i < 1_000_000; i++) {
                // Although shared map offers synchronized access,
                //   it doesn't protect from CheckThenAct RaceConditions.
                //
                if(sharedMap.containsKey("key")){
                    String value = sharedMap.remove("key");
                    /*
                        Problem:
                            Entering this 'if' will never happen.
                            If thread 1 and 2 are executing, they are both going to
                               check that the SharedMap contained the key, but one
                               is going to change the remove before the other one
                               therefore, performing the remove operation twice,
                               making us enter this if.
                    */
                    if(value == null){
                        System.out.println("For iteration: i = " + i + ": We were expecting a value for key, " +
                                "but it waS null");
                    }
                }else{
                    sharedMap.put("key", "valueABC");
                }

            }
        };
    }
}