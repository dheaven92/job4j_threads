package ru.job4j.threadpool;

import ru.job4j.waitnotfiy.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;

public class ThreadPool {

    private final static int CORE_SIZE = Runtime.getRuntime().availableProcessors();
    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>(CORE_SIZE);

    public ThreadPool() {
        for (int i = 0; i < CORE_SIZE; i++) {
            threads.add(new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        tasks.poll().run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
            }));
        }
        threads.forEach(Thread::start);
    }

    public void work(Runnable task) throws InterruptedException {
        tasks.offer(task);
    }

    public void shutdown() {
        threads.forEach(Thread::interrupt);
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadPool threadPool = new ThreadPool();
        System.out.println("ThreadPool gets tasks");
        for (int i = 0; i < 10; i++) {
            threadPool.work(new Task());
        }
        Thread.sleep(2000);
        System.out.println("ThreadPool gets tasks");
        for (int i = 0; i < 10; i++) {
            threadPool.work(new Task());
        }
        Thread.sleep(2000);
        System.out.println("ThreadPool stops");
        threadPool.shutdown();
    }

    private static class Task implements Runnable {

        @Override
        public void run() {
            System.out.println("Task starts");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Task ends");
        }
    }
}
