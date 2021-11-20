package ru.job4j.nonblockingalgorithm;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class CASCount {

    private final AtomicReference<Integer> count = new AtomicReference<>(0);

    public void increment() {
        int localCount;
        do {
            localCount = count.get();
        } while (!count.compareAndSet(localCount, localCount + 1));
    }

    public int get() {
        return count.get();
    }
}
