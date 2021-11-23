package ru.job4j.threadpool;

import org.junit.Test;

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
        ParallelFindIndex<String> parallelFindIndex = new ParallelFindIndex<>(arr, "spring", 0, arr.length);
        assertEquals(16, parallelFindIndex.findIndex());
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
        ParallelFindIndex<String> parallelFindIndex = new ParallelFindIndex<>(arr, "spring", 0, arr.length);
        assertEquals(-1, parallelFindIndex.findIndex());
    }
}