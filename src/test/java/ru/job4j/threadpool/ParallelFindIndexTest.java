package ru.job4j.threadpool;

import org.junit.Test;

import java.util.concurrent.ForkJoinPool;

import static org.junit.Assert.assertEquals;

public class ParallelFindIndexTest {

    @Test
    public void whenFind() {
        String[] arr = new String[] {
                "rhythm",
                "offender",
                "introduction",
                "belt",
                "sunshine",
                "sample",
                "prove",
                "soldier",
                "object",
                "management",
                "charter",
                "instinct",
                "cope",
                "formation",
                "execute",
                "thirsty",
                "spring",
                "unanimous",
                "perforate",
                "face",
                "harass",
                "coal"
        };
        ParallelFindIndex<String> parallelFindIndex
                = new ParallelFindIndex<>(arr, "spring", 0, arr.length);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        assertEquals(Integer.valueOf(16), forkJoinPool.invoke(parallelFindIndex));
    }

    @Test
    public void whenDontFind() {
        String[] arr = new String[] {
                "rhythm",
                "offender",
                "introduction",
                "belt",
                "sunshine",
                "sample",
                "prove"
        };
        ParallelFindIndex<String> parallelFindIndex
                = new ParallelFindIndex<>(arr, "spring", 0, arr.length);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        assertEquals(Integer.valueOf(-1), forkJoinPool.invoke(parallelFindIndex));
    }
}