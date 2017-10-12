package com.xiaohongxiedaima.demo.algorithm.dnf;

/**
 * Created by liusheng on 17-10-12.
 */
public class StringTermIndex<R> extends AbstractTermIndex<String, R> {

    protected R add(String value, R record) {
        return this.index.put(value, record);
    }

    protected boolean contains(String value) {
        return this.index.containsKey(value);
    }

    protected R get(String value) {
        return this.index.get(value);
    }

    protected R delete(String value) {
        return this.index.remove(value);
    }
}
