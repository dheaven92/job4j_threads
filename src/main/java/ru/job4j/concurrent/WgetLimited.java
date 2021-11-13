package ru.job4j.concurrent;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class WgetLimited implements Runnable {

    private final String url;
    private final int speed;

    public WgetLimited(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream out = new FileOutputStream(Paths.get(new URL(url).getPath()).getFileName().toString())) {
            byte[] dataBuffer = new byte[speed];
            int bytesRead;
            long startTime = System.currentTimeMillis();
            long lastLimitTime = System.currentTimeMillis();
            long bytesTotalWritten = 0;
            long bytesLimitWritten = 0;
            while ((bytesRead = in.read(dataBuffer, 0, speed)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
                bytesTotalWritten += bytesRead;
                bytesLimitWritten += bytesRead;
                if (bytesLimitWritten >= speed) {
                    long deltaTime = System.currentTimeMillis() - lastLimitTime;
                    if (deltaTime < 1000) {
                        Thread.sleep(1000 - deltaTime);
                    }
                    bytesLimitWritten = 0;
                    lastLimitTime = System.currentTimeMillis();
                }
            }
            System.out.printf("Downloading %s KB took %s s with speed %s KB/s",
                    convertBytesToKiloBytes(bytesTotalWritten),
                    (System.currentTimeMillis() - startTime) / 1000,
                    convertBytesToKiloBytes(speed));
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            } else {
                e.printStackTrace();
            }
        }
    }

    private static void validateArgs(int argsNum) {
        if (argsNum != 2) {
            throw new IllegalArgumentException("You must provide 2 arguments (file name and speed limit)");
        }
    }

    private static long convertBytesToKiloBytes(long bytes) {
        return Math.round((double) bytes / 1024);
    }

    public static void main(String[] args) throws InterruptedException {
        validateArgs(args.length);
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new WgetLimited(url, speed));
        wget.start();
        wget.join();
    }
}
