package com.boris.threads.wait_notify;

public class Data {
    private String packet;

    //true if receiver should wait
    //false if sender should wait
    private boolean transfer = true;

    public  synchronized String receive() {
        while (transfer) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread " + Thread.currentThread().getName() + " interrupted");
            }
        }
        transfer = true;
        String returnPacket = packet;
        notifyAll();
        return returnPacket;
    }

    public synchronized void send(String packet) {
        while (!transfer) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread " + Thread.currentThread().getName() + " interrupted");
            }
        }
        transfer = false;
        this.packet = packet;
        notify();
    }
}
