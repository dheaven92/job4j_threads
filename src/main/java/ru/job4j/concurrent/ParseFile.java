package ru.job4j.concurrent;

import net.jcip.annotations.ThreadSafe;

import java.io.*;
import java.util.function.Predicate;

@ThreadSafe
public class ParseFile {

    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    public synchronized String getAllContent() throws IOException {
        return getContent(c -> true);
    }

    public synchronized String getContentWithoutUnicode() throws IOException {
        return getContent(c -> c < 0x80);
    }

    private String getContent(Predicate<Character> filter) throws IOException {
        try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {
            StringBuilder output = new StringBuilder();
            int data;
            while ((data = in.read()) != -1) {
                char dataChar = (char) data;
                if (filter.test(dataChar)) {
                    output.append(dataChar);
                }
            }
            return output.toString();
        }
    }
}
