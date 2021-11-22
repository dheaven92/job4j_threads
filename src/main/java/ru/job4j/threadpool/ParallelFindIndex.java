package ru.job4j.threadpool;

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

    @Override
    protected Integer compute() {
        if (end - start < 10) {
            return find(arr, key);
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

    private static <T> int find(T[] arr, T key) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(key)) {
                return i;
            }
        }
        return -1;
    }
}
