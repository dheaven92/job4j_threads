package ru.job4j.threadpool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelFindIndex<T> extends RecursiveTask<Integer> {

    private final T[] arr;
    private final T key;
    private final int start;
    private final int end;

    public ParallelFindIndex(T[] arr, T key, int start, int end) {
        this.arr = arr;
        this.key = key;
        this.start = start;
        this.end = end;
    }

    public int findIndex() {
        return new ForkJoinPool().invoke(this);
    }

    @Override
    protected Integer compute() {
        if (end - start < 10) {
            return find();
        }
        int mid = (start + end) / 2;
        ParallelFindIndex<T> leftFind = new ParallelFindIndex<>(arr, key, start, mid);
        ParallelFindIndex<T> rightFind = new ParallelFindIndex<>(arr, key, mid + 1, end);
        leftFind.fork();
        rightFind.fork();
        int leftIndex = leftFind.join();
        int rightIndex = rightFind.join();
        return Math.max(leftIndex, rightIndex);
    }

    private int find() {
        for (int i = start; i < end; i++) {
            if (arr[i].equals(key)) {
                return i;
            }
        }
        return -1;
    }
}
