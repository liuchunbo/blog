package com.example.guava.cache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;

public class SoftReferenceLruCache<K, V> implements LruCache<K, V> {
    private static class InternalLruCache<K, V> extends LinkedHashMap<K, SoftReference<V>> {
        private final int limit;

        public InternalLruCache(int limit) {
            super(16, 0.75f, true);
            this.limit = limit;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, SoftReference<V>> eldest) {return this.size() > limit;}
    }

    private final int limit;

    private final InternalLruCache<K, V> cache;

    public SoftReferenceLruCache(int limit) {
        this.limit = limit;
        this.cache = new InternalLruCache<>(limit);
    }

    @Override
    public void put(K key, V value) {this.cache.put(key, new SoftReference<>(value));}

    @Override
    public V get(K key) {
        SoftReference<V> ref = this.cache.get(key);
        if (null == ref) {
            return null;
        }
        return ref.get();
    }

    @Override
    public void remove(K key) {this.cache.remove(key);}

    @Override
    public int size() {return this.cache.size();}

    @Override
    public void clear() {this.cache.clear();}

    @Override
    public int limit() {return this.limit;}
}
