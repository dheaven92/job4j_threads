package ru.job4j.concurrent;

public class Cache {

    private static Cache cache;

    public synchronized static Cache instanceOf() {
        if (cache == null) {
            cache = new Cache();
        }
        return cache;
    }
}