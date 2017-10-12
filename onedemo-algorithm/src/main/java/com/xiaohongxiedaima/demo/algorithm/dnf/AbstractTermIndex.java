package com.xiaohongxiedaima.demo.algorithm.dnf;

import java.util.Map;
import java.util.Set;

/**
 * Created by liusheng on 17-10-12.
 */
abstract public class AbstractTermIndex<V, R> {

    protected String name;
    protected Map<V, R> index;

    public AbstractTermIndex() {
    }

    protected abstract R add(V value, R record);
    protected abstract boolean contains(V value);
    protected abstract R get(V value);
    protected abstract R delete(V value);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<V, R> getIndex() {
        return index;
    }

    public void setIndex(Map<V, R> index) {
        this.index = index;
    }
}
