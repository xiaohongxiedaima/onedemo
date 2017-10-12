package com.xiaohongxiedaima.demo.algorithm.dnf;

/**
 * Created by liusheng on 17-10-12.
 */
public class IntTermIndex<R> extends AbstractTermIndex<Integer, R> {
    protected R add(Integer value, R record) {
        return this.index.put(value, record);
    }

    protected boolean contains(Integer value) {
        return this.index.containsKey(value);
    }

    protected R get(Integer value) {
        return this.index.get(value);
    }

    protected R delete(Integer value) {
        return this.index.remove(value);
    }
}
