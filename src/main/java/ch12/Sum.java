package ch12;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class SequentialSum {


    public long sum(int[] nrs) {
        int result = 0;
        for (int n : nrs) {
            result = result + n;
        }

        return result;
    }
}


class FJSum extends RecursiveTask<Long> {

    private SequentialSum sequentialSum = new SequentialSum();
    private int cores;
    private final int startIdx;
    private final int endIdx;
    private int[] nrs;
    private int minUnitOfWork;

    FJSum(int cores, int[] nrs, int startIdx, int endIdx, int minUnitOfWork) {
        this.cores = cores;
        this.nrs = nrs;
        this.startIdx = startIdx;
        this.endIdx = endIdx;
        this.minUnitOfWork = minUnitOfWork;
    }

    public long sum(int[] nrs, int startIdx, int endIdx) {
        int result = 0;
        for (int i = startIdx; i <= endIdx; i++) {
            result = result + nrs[i];
        }
        return result;
    }

    @Override
    protected Long compute() {
        if (this.endIdx == this.startIdx) {
            return 1L;
        }
        int length = (this.endIdx - this.startIdx) + 1;
        if (length <= minUnitOfWork) {
            return sum(nrs, startIdx, endIdx);
        } else {
            long result = 0;

            int nrPartitions = (int) Math.ceil(length * 1.0 / minUnitOfWork * 1.0);
            int increment = length / nrPartitions;
            FJSum[] forks = new FJSum[nrPartitions];

            int j = 0;
            while (j < nrPartitions) {
                int start = startIdx + (increment * j);
                int end = start + increment - 1;
                forks[j] = new FJSum(cores, nrs, start, end, minUnitOfWork);
                j++;
            }
            invokeAll(forks);
            for (FJSum task : forks) {
                result = result + task.join();
            }
            return result;
        }
    }
}

public class Sum {

    Random random = new Random();


    private int[] createRange() {
        int length = random.nextInt();
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = Math.abs(random.nextInt());
        }
        return array;
    }

    public static void main(String[] args) {
        ForkJoinPool fj = new ForkJoinPool();
        int[] numbers = new int[17];
        Arrays.fill(numbers, 1);
        SequentialSum sequentialSum = new SequentialSum();
        long rs = sequentialSum.sum(numbers);

        System.out.println("Sequential sum result: " + rs);
        FJSum sum = new FJSum(Runtime.getRuntime().availableProcessors(), numbers, 0, numbers.length - 1, 2);
        rs = sum.compute();
        System.out.println("Parallel sum result: " + rs);
    }
}