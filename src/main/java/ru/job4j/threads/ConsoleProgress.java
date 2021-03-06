package ru.job4j.threads;

public class ConsoleProgress implements Runnable {

    @Override
    public void run() {
        try {
            int index = 0;
            while (!Thread.currentThread().isInterrupted()) {
                char[] process = new char[] {'\\', '|', '/'};
                System.out.print("\r load: " + process[index++]);
                if (index == process.length) {
                    index = 0;
                }
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(10000);
        progress.interrupt();
    }
}
