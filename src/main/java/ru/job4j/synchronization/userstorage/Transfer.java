package ru.job4j.synchronization.userstorage;

public interface Transfer {

    void transfer(int fromId, int toId, int amount);
}
