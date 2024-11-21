package com.revision;

public class SynchronizedCounter {
    int counter = 0; // instance variable

    public SynchronizedCounter(int count) {
        this.counter = count;
    }

    public synchronized void incrementCounter(){
        counter++;
    }

    public int getCounter(){
        return counter;
    }
}
