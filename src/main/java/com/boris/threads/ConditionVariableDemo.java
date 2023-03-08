package com.boris.threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class HungryPerson extends Thread {
    private int personId;
    private static Lock slowCookerLid = new ReentrantLock();
    private static int servings = 11;
    private static Condition soupTaken = slowCookerLid.newCondition();

    public  HungryPerson(int personId) {
        this.personId = personId;
    }

    @Override
    public void run() {
        while (servings > 0) {
            System.out.println("Person " + personId + " locking the lid");
            slowCookerLid.lock();
            try {
                //System.out.println("Person " + personId + ", servings = " + servings + " servings % 2 = " + (servings % 2));
                //System.out.println("(personId != servings % 2) = " + (personId != servings % 2));
                //check if it's your turn
                //if((personId != servings % 2) && servings > 0) {
                while (((personId != servings % 5) && servings > 0)) {
                    System.out.format("Person %d checked... then put the lid back. Waiting now\n", personId);
                    soupTaken.await();
                }
                if(servings > 0) {
                    servings--;
                    System.out.format("Person %d took some soup! Servings left: %d and signaling\n", personId, servings);
                    soupTaken.signalAll();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println("Person " + personId + " unlocking the lid");
                slowCookerLid.unlock();
            }
        }
    }
}

public class ConditionVariableDemo {
    public static void main(String[] args) {
        for(int i=0;i<5;i++) {
            new HungryPerson(i).start();
        }
    }
}
