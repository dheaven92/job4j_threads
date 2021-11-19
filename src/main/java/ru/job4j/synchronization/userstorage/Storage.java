package ru.job4j.synchronization.userstorage;

public interface Storage<T> {

    boolean add(T item);

    boolean update(T item);

    boolean delete(T item);
}
