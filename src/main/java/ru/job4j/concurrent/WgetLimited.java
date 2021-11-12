package ru.job4j.concurrent;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class WgetLimited implements Runnable {

    private final static int PART_SIZE = 1024;
    private final String url;
    private final int speed;

    public WgetLimited(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream out = new FileOutputStream(getFileNameFromUrl(url))) {
            byte[] dataBuffer = new byte[PART_SIZE];
            int bytesRead;
            long startTotalTime = System.currentTimeMillis();
            long startPartTime = startTotalTime;
            long size = 0;
            while ((bytesRead = in.read(dataBuffer, 0, PART_SIZE)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
                size += bytesRead;
                long endPartTime = System.currentTimeMillis() - startPartTime;
                startPartTime = System.currentTimeMillis();
                if (endPartTime < speed) {
                    Thread.sleep(speed - endPartTime);
                }
            }
            System.out.printf("Downloading %s b took %s ms with speed %s b/s",
                    size, (System.currentTimeMillis() - startTotalTime), speed);
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            } else {
                e.printStackTrace();
            }
        }
    }

    private String getFileNameFromUrl(String url) {
        try {
            String[] parts = url.split("/");
            return "tmp_" + parts[parts.length - 1];
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid url format");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new WgetLimited(url, speed));
        wget.start();
        wget.join();
    }
}
