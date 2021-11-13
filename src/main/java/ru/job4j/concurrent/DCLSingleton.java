package ru.job4j.concurrent;

public class DCLSingleton {

    private volatile static DCLSingleton instance;

    private DCLSingleton() {
    }

    public static DCLSingleton instanceOf() {
        if (instance == null) {
            synchronized (DCLSingleton.class) {
                if (instance == null) {
                    instance = new DCLSingleton();
                }
            }
        }
        return instance;
    }
}
