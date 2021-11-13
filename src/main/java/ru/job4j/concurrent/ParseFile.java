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

    public synchronized String getContent(Predicate<Character> filter) throws IOException {
        try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {
            String output = "";
            int data;
            while ((data = in.read()) > 0) {
                char dataChar = (char) data;
                if (filter.test(dataChar)) {
                    output += dataChar;
                }
            }
            return output;
        }
    }
}
