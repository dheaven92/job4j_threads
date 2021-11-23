package ru.job4j.threadpool.rowcolsum;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RowColSum {

    public static Sums[] sum(int[][] matrix) {
        Sums[] sums = new Sums[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            int rowSum = 0;
            int colSum = 0;
            for (int j = 0; j < matrix.length; j++) {
                rowSum += matrix[i][j];
                colSum += matrix[j][i];
            }
            sums[i] = new Sums(rowSum, colSum);
        }
        return sums;
    }

    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        Sums[] sums = new Sums[matrix.length];
        Map<Integer, CompletableFuture<Sums>> futures = new HashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            futures.put(i, getTask(matrix, i));
        }
        for (Integer key : futures.keySet()) {
            sums[key] = futures.get(key).get();
        }
        return sums;
    }

    public static CompletableFuture<Sums> getTask(int[][] matrix, int i) {
        return CompletableFuture.supplyAsync(() -> {
            int rowSum = 0;
            int colSum = 0;
            for (int j = 0; j < matrix.length; j++) {
                rowSum += matrix[i][j];
                colSum += matrix[j][i];
            }
            return new Sums(rowSum, colSum);
        });
    }
}
