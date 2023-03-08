package com.boris.threads;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.*;

class Shopper2 extends Thread {
    public static int bagsOfChips = 1; //start with one on the list
    private static Lock pencil = new ReentrantLock();
    private static CyclicBarrier fistBump = new CyclicBarrier(10);

    public Shopper2(String name) {
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
            try {
                fistBump.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        } else {
            try {
                fistBump.await();
            } catch (InterruptedException | BrokenBarrierException e) {
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

public class BarrierDemo {
    public static void main(String[] args) throws  InterruptedException{
        Shopper2[] shoppers = new Shopper2[10];
        for(int i=0;i<shoppers.length/2;i++) {
            shoppers[2*i] = new Shopper2("Barron-" + i);
            shoppers[2*i+1] = new Shopper2("Olivia-" + i);
        }
        for(Shopper2 s : shoppers)
            s.start();
        for(Shopper2 s : shoppers)
            s.join();
        System.out.println("We need to buy " + Shopper2.bagsOfChips + " bags of chips");
    }
}
