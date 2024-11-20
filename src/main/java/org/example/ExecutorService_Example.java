package org.example;

import java.util.concurrent.*;

public class ExecutorService_Example {
    public static void main(String[] args) {


        /* I understood better what executorService actually does with this video:
            Java ExecutorService - Part 1 - Introduction: https://www.youtube.com/watch?v=6Oo-9Can3H8&ab_channel=DefogTech
         */


        // Chapter1:
        // Executor Services is a Thread pool that you can submit tasks to do concurrent execution.
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        /*
            Yes, when you call Executors.newFixedThreadPool(10), you are obtaining a concrete
                implementation of the ExecutorService interface, that limits to 10 pool threads.
            Specifically, this method returns an instance of ThreadPoolExecutor,
                which is the underlying implementation used by most factory methods in the Executors class.
         */

        executorService.execute(newRunnable("Task1"));
        executorService.execute(newRunnable("Task2"));
        executorService.execute(newRunnable("Task3"));

        executorService.shutdown();

        // Chapter2:
        // Two type's of implementations for ExecutorService interface:
        //      ThreadPoolExecutor (Default), We can customize Number of Threads, etc
        //      ScheduledThreadPoolExecutor, in contrary, it tries to do the tasks in scheduled time,
        //             not as fast as possible

        // Chapter 3:
        // .Submit vs .Execute

        ExecutorService executorService2 = Executors.newFixedThreadPool(10);

        // Submit returns Future
        Future future = executorService2.submit(newRunnable("Task4"));

        // Is Task done?. Normally it's not, since Code instruction normally is faster.
        System.out.println(future.isDone());

        try{
            // Returns a result from the task. But runnable's return void, therefore, in this case returns null.
            // Main threads blocks until gets a result from runnable.
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        executorService2.shutdown();


    }

    private static Runnable newRunnable(String taskString){
        return new Runnable() {
            public void run() {
                System.out.println(Thread.currentThread().getName() + "TaskString: " + taskString);
            }
        };
    }
}
