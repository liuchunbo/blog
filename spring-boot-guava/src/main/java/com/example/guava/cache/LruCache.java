package com.example.guava.cache;

public interface LruCache<K, V> {

    void put(K key, V value);

    V get(K key);

    void remove(K key);

    int size();

    void clear();

    int limit();
}

