package org.example;

public class SynchronizedCounter {

    private int count;

    public SynchronizedCounter(int count) {
        this.count = count;
    }

    public synchronized void increment() {
        this.count++;
    }

    public synchronized long getCount(){
        return this.count;
    }
}
