package ru.job4j.nonblockingalgorithm.cache;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

public class CacheTest {

    @Test
    public void add() {
        Cache cache = new Cache();
        Base user1 = new Base(1, 1);
        boolean added = cache.add(user1);
        assertThat(added, is(true));
        assertThat(cache.get(user1.getId()), is(user1));
    }

    @Test
    public void update() {
        Cache cache = new Cache();
        Base user1 = new Base(1, 1);
        cache.add(user1);
        boolean updated = cache.update(user1);
        assertThat(updated, is(true));
        assertThat(cache.get(user1.getId()).getVersion(), is(user1.getVersion() + 1));
    }

    @Test
    public void delete() {
        Cache cache = new Cache();
        Base user1 = new Base(1, 1);
        cache.add(user1);
        cache.delete(user1);
        assertThat(cache.get(user1.getId()), is(nullValue()));
    }
}