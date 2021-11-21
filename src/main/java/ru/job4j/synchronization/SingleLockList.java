package ru.job4j.synchronization;

import net.jcip.annotations.ThreadSafe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ThreadSafe
public class SingleLockList<E> implements Iterable<E> {

    private final List<E> list;

    public SingleLockList(List<E> list) {
        this.list = copy(list);
    }

    public synchronized void add(E value) {
        list.add(value);
    }

    public synchronized E get(int index) {
        return list.get(index);
    }

    @Override
    public synchronized Iterator<E> iterator() {
        return copy(list).iterator();
    }

    private synchronized List<E> copy(List<E> list) {
        return new ArrayList<>(list);
    }
}
