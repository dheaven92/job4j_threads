package ru.job4j.concurrent;

import net.jcip.annotations.ThreadSafe;

import java.io.*;

@ThreadSafe
public class FileSaver {

    private final File file;

    public FileSaver(File file) {
        this.file = file;
    }

    public synchronized void saveContent(String content) throws IOException {
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            for (int i = 0; i < content.length(); i += 1) {
                out.write(content.charAt(i));
            }
        }
    }
}
