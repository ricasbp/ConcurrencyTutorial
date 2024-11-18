package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeadLock_ExampleTest {
    @Test
    void getDeadLockFixedTest(){
            DeadLock_Example deadLock = new DeadLock_Example();

            int count1 = 0;
            int count2 = 0;

            // We want to increment both counters
            // with each one of the threads we created
            deadLock.incrementCounters(count1, count2);
            Assertions.assertEquals(count1, 2);
            Assertions.assertEquals(count2, 2);

    }
}