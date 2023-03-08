package com.boris.threads;

import java.util.concurrent.*;

class RecursiveSum2 extends RecursiveTask<Long> {
    private long lo, hi;

    public RecursiveSum2(long lo, long hi) {
        this.lo = lo;
        this.hi = hi;
    }
    @Override
    protected Long compute() {
        if(hi - lo <= 100_000) { //base case threshold
            long total = 0;
            for(long i=lo;i<=hi;i++) total += i;
            return total;
        } else {
            long mid = (hi + lo)/2; //middle index for split
            RecursiveSum2 left = new RecursiveSum2(lo, mid);
            RecursiveSum2 right = new RecursiveSum2(mid + 1, hi);
            left.fork(); //forked thread computes left half
            return right.compute() + left.join(); //current thread computes right half
        }
    }
}

public class DivideAndConquerDemo {
    public static void main(String[] args) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        Long total = pool.invoke(new RecursiveSum2(0, 1_000_000_000));
        pool.shutdown();
        System.out.println("total sum = " + total);
    }
}
