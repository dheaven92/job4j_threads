package ru.job4j.commonresources;

public class Cache {

    private static Cache cache;

    public synchronized static Cache instanceOf() {
        if (cache == null) {
            cache = new Cache();
        }
        return cache;
    }
}
