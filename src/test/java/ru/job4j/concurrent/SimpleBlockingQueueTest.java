package ru.job4j.concurrent;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SimpleBlockingQueueTest {

    private CopyOnWriteArrayList<Integer> buffer;
    private SimpleBlockingQueue<Integer> queue;

    @Before
    public void init() {
        this.buffer = new CopyOnWriteArrayList<>();
        this.queue = new SimpleBlockingQueue<>(10);
    }

    @Test
    public void whenMultipleConsumersAndOneProducer() throws InterruptedException {
        Thread producer = new Thread(new Producer());
        producer.start();
        Thread consumer1 = new Thread(new Consumer());
        Thread consumer2 = new Thread(new Consumer());
        consumer1.start();
        consumer2.start();
        producer.join();
        consumer1.interrupt();
        consumer1.join();
        consumer2.interrupt();
        consumer2.join();
        assertThat(buffer, is(List.of(0, 1, 2, 3, 4)));
    }

    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        Thread producer = new Thread(new Producer());
        producer.start();
        Thread consumer = new Thread(new Consumer());
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        assertThat(buffer, is(List.of(0, 1, 2, 3, 4)));
    }

    @Test
    public void whenOneConsumerAndOneProducer() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(2);
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 2; i++) {
                    queue.poll();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 4; i++) {
                    queue.offer(i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        consumer.start();
        producer.start();
        consumer.join();
        producer.join();
        assertEquals(List.of(2, 3), queue.getQueue());
    }

    private class Consumer implements Runnable {

        @Override
        public void run() {
            while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                try {
                    buffer.add(queue.poll());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private class Producer implements Runnable {

        @Override
        public void run() {
            IntStream.range(0, 5).forEach(i -> {
                try {
                    queue.offer(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}