package ru.job4j.concurrent.userstorage;

public interface Transfer {

    void transfer(int fromId, int toId, int amount);
}
