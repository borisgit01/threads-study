package com.boris.threads;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Shopper3 extends Thread {
    public static int bagsOfChips = 1; //start with one on the list
    private static Lock pencil = new ReentrantLock();
    private static CountDownLatch fistBump = new CountDownLatch(5);

    public Shopper3(String name) {
        this.setName(name);
    }

    @Override
    public void  run() {
        if(this.getName().contains("Olivia")) {
            pencil.lock();
            try {
                bagsOfChips += 3;
                System.out.println(this.getName() + " added three bags of chips");
            } finally {
                pencil.unlock();
            }
            fistBump.countDown();
        } else {
            try {
                fistBump.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pencil.lock();
            try {
                bagsOfChips *= 2;
                System.out.println(this.getName() + " doubled the bags of chips");
            } finally {
                pencil.unlock();
            }
        }
    }
}

public class CountDownLatchDemo {
    public static void main(String[] args) throws  InterruptedException{
        Shopper3[] shoppers = new Shopper3[10];
        for(int i=0;i<shoppers.length/2;i++) {
            shoppers[2*i] = new Shopper3("Barron-" + i);
            shoppers[2*i+1] = new Shopper3("Olivia-" + i);
        }
        for(Shopper3 s : shoppers)
            s.start();
        for(Shopper3 s : shoppers)
            s.join();
        System.out.println("We need to buy " + Shopper3.bagsOfChips + " bags of chips");
    }
}
