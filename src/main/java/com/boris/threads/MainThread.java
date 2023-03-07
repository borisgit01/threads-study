package com.boris.threads;

public class MainThread {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName());
        for(int count=1;count<=3;count++) {
            FirstThread thread = new FirstThread(count);
            thread.start();
        }
        MyRunnable myRunnable = new MyRunnable();
        Thread thread = new Thread(myRunnable);
        thread.start();
    }
}
