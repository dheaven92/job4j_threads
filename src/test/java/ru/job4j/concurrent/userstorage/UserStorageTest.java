package ru.job4j.concurrent.userstorage;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserStorageTest {

    private UserStorage storage;

    @Before
    public void init() {
        storage = new UserStorage();
    }

    @Test
    public void transferInOneThread() {
        User user1 = new User(1, 8000);
        User user2 = new User(2, 9600);
        storage.add(user1);
        storage.add(user2);
        storage.transfer(1, 2, 500);
        assertEquals(7500, user1.getAmount());
        assertEquals(10100, user2.getAmount());
    }

    @Test
    public void transferInTwoThreads() throws InterruptedException {
        User user1 = new User(1, 8000);
        User user2 = new User(2, 9600);
        storage.add(user1);
        storage.add(user2);
        Thread thread1 = new Thread(() -> storage.transfer(1, 2, 500));
        Thread thread2 = new Thread(() -> storage.transfer(2, 1, 500));
        thread1.join();
        thread2.join();
        assertEquals(8000, user1.getAmount());
        assertEquals(9600, user2.getAmount());
    }

    @Test
    public void transferAllMoney() {
        User user1 = new User(1, 8000);
        User user2 = new User(2, 9600);
        storage.add(user1);
        storage.add(user2);
        storage.transfer(1, 2, 8000);
        assertEquals(0, user1.getAmount());
        assertEquals(17600, user2.getAmount());
    }

    @Test
    public void transferTooMuchMoney() {
        User user1 = new User(1, 8000);
        User user2 = new User(2, 9600);
        storage.add(user1);
        storage.add(user2);
        storage.transfer(1, 2, 8001);
        assertEquals(8000, user1.getAmount());
        assertEquals(9600, user2.getAmount());
    }
}