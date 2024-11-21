package com.revision;

public class RaceConditionRunnable {

    public static Runnable getRunnable(SynchronizedCounter counter, int incrementCount) {
        return () -> {
            for(int i = 0; i < incrementCount; i++) {
                synchronized (counter) {
                    if(counter.getCounter() >= 1){
                        counter.incrementCounter();
                    }
                    counter.incrementCounter();
                    System.out.println(counter.getCounter());
                }
            }
            System.out.println("Counter value= " + counter.getCounter());
        };
    }
}
