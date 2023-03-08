package com.boris.threads;

import java.util.concurrent.*;

class VegetableChopper extends Thread {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " chopped a vegetable!");
    }
}

public class ThreadPoolDemo {
    public static void main(String[] args) {
        int numProcs = Runtime.getRuntime().availableProcessors();
        System.out.println("have " + numProcs + " processors here");
        ExecutorService pool = Executors.newFixedThreadPool(numProcs);
        for (int i=0;i<100;i++) pool.submit(new VegetableChopper());
        pool.shutdown();
    }
}
