package ru.job4j.concurrent.userstorage;

import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;

@ThreadSafe
public class UserStorage implements Storage<User>, Transfer {

    private final Map<Integer, User> storage = new HashMap<>();

    @Override
    public synchronized boolean add(User user) {
        return storage.putIfAbsent(user.getId(), user) == null;
    }

    @Override
    public synchronized boolean update(User user) {
        return storage.replace(user.getId(), user) != null;
    }

    @Override
    public synchronized boolean delete(User user) {
        return storage.remove(user.getId(), user);
    }

    @Override
    public synchronized void transfer(int fromId, int toId, int amount) {
        User userFrom = storage.get(fromId);
        User userTo = storage.get(toId);
        if (userFrom != null && userTo != null) {
            int userFromAmountUpdated = userFrom.getAmount() - amount;
            if (userFromAmountUpdated >= 0) {
                userFrom.setAmount(userFromAmountUpdated);
                userTo.setAmount(userTo.getAmount() + amount);
            }
        }
    }
}
