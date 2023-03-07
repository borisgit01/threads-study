package com.boris.threads;

public class ConditionVariableDemo {
    public static void main(String[] args) {
        for(int i=0;i<5;i++) {
            new HungryPerson(i).start();
        }
    }

}
