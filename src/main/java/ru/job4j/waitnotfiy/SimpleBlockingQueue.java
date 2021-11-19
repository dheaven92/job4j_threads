package ru.job4j.waitnotfiy;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<E> {

    @GuardedBy("this")
    private final Queue<E> queue = new LinkedList<>();

    private final int limit;

    public SimpleBlockingQueue(int limit) {
        this.limit = limit;
    }

    public void offer(E value) throws InterruptedException {
        synchronized (this) {
            while (queue.size() == limit) {
                wait();
            }
            queue.offer(value);
            notify();
        }
    }

    public E poll() throws InterruptedException {
        synchronized (this) {
            while (queue.size() == 0) {
                wait();
            }
            E value = queue.poll();
            notify();
            return value;
        }
    }

    public synchronized int size() {
        return queue.size();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}
