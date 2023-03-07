package com.boris.threads;

import javax.swing.plaf.TableHeaderUI;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

class SoupProducer extends Thread {
    private BlockingQueue servingLine;

    public SoupProducer(BlockingQueue servingLine) {
        this.servingLine = servingLine;
    }

    @Override
    public void run() {
        for(int i=0;i<20;i++) {
            try {
                servingLine.add("Bolw # " + i);
                System.out.println("served bowl #" + i + " - remaining capacity: " + servingLine.remainingCapacity());
                Thread.sleep(200);
            } catch (Exception e) {e.printStackTrace();}
        }
        servingLine.add("no soup for you");
        servingLine.add("no soup for you");
    }
}

class SoupConsumer extends Thread {
    private BlockingQueue servingLine;

    public SoupConsumer(BlockingQueue servingLine) {
        this.servingLine = servingLine;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("taking from queue");
                String bowl = (String)servingLine.take();
                if(bowl.equals("no soup for you")) break;
                System.out.println("Ate " + bowl);
                Thread.sleep(300);
            } catch (Exception e) {e.printStackTrace();}
        }
    }
}

public class ProducerConsumerDemo {
    public static void main(String[] args) {
        BlockingQueue servingLine = new ArrayBlockingQueue<String>(5);
        new SoupConsumer(servingLine).start();
        new SoupConsumer(servingLine).start();
        new SoupProducer(servingLine).start();
    }
}
