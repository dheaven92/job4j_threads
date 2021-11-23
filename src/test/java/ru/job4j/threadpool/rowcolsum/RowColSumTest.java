package ru.job4j.threadpool.rowcolsum;

import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertArrayEquals;

public class RowColSumTest {

    @Test
    public void sum() {
        int[][] matrix = new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        Sums[] expected = new Sums[] {
                new Sums(6, 12),
                new Sums(15, 15),
                new Sums(24, 18)
        };
        Sums[] actual = RowColSum.sum(matrix);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void asyncSum() throws ExecutionException, InterruptedException {
        int[][] matrix = new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        Sums[] expected = new Sums[] {
                new Sums(6, 12),
                new Sums(15, 15),
                new Sums(24, 18)
        };
        Sums[] actual = RowColSum.asyncSum(matrix);
        assertArrayEquals(expected, actual);
    }
}