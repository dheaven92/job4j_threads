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
    public synchronized boolean update(User updatedUser) {
        User oldUser = storage.get(updatedUser.getId());
        if (oldUser != null) {
            oldUser.setAmount(updatedUser.getAmount());
            storage.put(oldUser.getId(), oldUser);
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean delete(User user) {
        return storage.remove(user.getId(), user);
    }

    @Override
    public synchronized void transfer(int fromId, int toId, int amount) {
        User userFrom = storage.get(fromId);
        User userTo = storage.get(toId);
        update(new User(userFrom.getId(), userFrom.getAmount() - amount));
        update(new User(userTo.getId(), userTo.getAmount() + amount));
    }
}
