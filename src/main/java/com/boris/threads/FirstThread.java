package com.boris.threads;

public class FirstThread extends Thread {
    private int number;

    public FirstThread(int number) {
        this.number = number;
    }
    @Override
    public void run() {
        for(int i=0; i<5; i++) {
            System.out.println(i + " from the thread " + number);
        }
    }
}
