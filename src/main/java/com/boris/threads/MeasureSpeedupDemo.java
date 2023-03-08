package com.boris.threads;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class RecursiveSum extends RecursiveTask<Long> {
    private long lo, hi;

    public RecursiveSum(long lo, long hi) {
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
            RecursiveSum left = new RecursiveSum(lo, mid);
            RecursiveSum right = new RecursiveSum(mid + 1, hi);
            left.fork(); //forked thread computes left half
            return right.compute() + left.join(); //current thread computes right half
        }
    }
}

public class MeasureSpeedupDemo {
    //Sequential implementation
    private static long sequentialSum(long lo, long hi) {
        long total = 0;
        for(long i=lo;i<=hi;i++) total += i;
        return total;
    }

    public static void main(String[] args) {
        final int NUM_EVAL_RUNS = 10;
        final long SUM_VALUE = 1_000_000_000L;

        System.out.println("Evaluating sequential implementation...");
        long sequentialResult = sequentialSum(0, SUM_VALUE); // "warm up"
        double sequentialTime = 0;
        for(int i=0;i<NUM_EVAL_RUNS;i++) {
            long start = System.currentTimeMillis();
            sequentialSum(0, SUM_VALUE);
            sequentialTime += System.currentTimeMillis() - start;
        }
        sequentialTime /= NUM_EVAL_RUNS;

        System.out.println("Evaluating parallel implementation...");
        ForkJoinPool pool = ForkJoinPool.commonPool();
        long parallelResult = pool.invoke(new RecursiveSum(0, SUM_VALUE)); // "warm up"
        pool.shutdown();
        double parallelTime = 0;
        for (int i=0;i<NUM_EVAL_RUNS;i++) {
            long start = System.currentTimeMillis();
            pool.invoke(new RecursiveSum(0, SUM_VALUE));
            pool.shutdown();
            parallelTime += System.currentTimeMillis() - start;
        }
        parallelTime /= NUM_EVAL_RUNS;

        if(sequentialResult != parallelResult)
            throw new Error("ERROR: sequential result and parallel result do not match!");
        System.out.format("Average Sequential Time: %.1f ms\n", sequentialTime);
        System.out.format("Average Parallel Time: %.1f ms\n", parallelTime);
        System.out.format("Speedup: %.2f \n", sequentialTime/parallelTime);
        System.out.format("Efficiency: %.2f%%\n", 100*(sequentialTime/parallelTime)/Runtime.getRuntime().availableProcessors());
    }
}
