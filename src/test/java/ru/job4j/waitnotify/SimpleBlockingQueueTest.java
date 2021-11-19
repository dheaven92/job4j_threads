package ru.job4j.waitnotify;

import org.junit.Test;
import ru.job4j.waitnotfiy.SimpleBlockingQueue;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SimpleBlockingQueueTest {

    @Test
    public void whenMultipleConsumersAndOneProducer() throws InterruptedException {
        CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(10);
        Thread producer = new Thread(new Producer(queue));
        producer.start();
        Thread consumer1 = new Thread(new Consumer(buffer, queue));
        Thread consumer2 = new Thread(new Consumer(buffer, queue));
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
        CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(10);
        Thread producer = new Thread(new Producer(queue));
        producer.start();
        Thread consumer = new Thread(new Consumer(buffer, queue));
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
        assertEquals(2, queue.size());
    }

    private static class Consumer implements Runnable {
        private final CopyOnWriteArrayList<Integer> buffer;
        private final SimpleBlockingQueue<Integer> queue;

        private Consumer(CopyOnWriteArrayList<Integer> buffer, SimpleBlockingQueue<Integer> queue) {
            this.buffer = buffer;
            this.queue = queue;
        }

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

    private static class Producer implements Runnable {
        private final SimpleBlockingQueue<Integer> queue;

        private Producer(SimpleBlockingQueue<Integer> queue) {
            this.queue = queue;
        }

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