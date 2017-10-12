package com.xiaohongxiedaima.demo.algorithm.dnf;

/**
 * Created by liusheng on 17-10-12.
 */
public class Pair<K, V> {

    private K key;
    private V value;

    public Pair() {
    }

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (!key.equals(pair.key)) return false;
        return value.equals(pair.value);
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
