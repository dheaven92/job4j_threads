package ru.job4j.nonblockingalgorithm;

import org.junit.Test;

import static org.junit.Assert.*;

public class CASCountTest {

    @Test
    public void whenMultipleThreads() throws InterruptedException {
        CASCount casCount = new CASCount();
        Thread thread1 = new Thread(casCount::increment);
        Thread thread2 = new Thread(casCount::increment);
        Thread thread3 = new Thread(casCount::increment);
        thread1.start();
        thread2.start();
        thread3.start();
        thread1.join();
        thread2.join();
        thread3.join();
        assertEquals(3, casCount.get());
    }
}