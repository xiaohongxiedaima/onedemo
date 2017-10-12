package com.xiaohongxiedaima.demo.algorithm.dnf;

import java.util.Arrays;
import java.util.Set;

/**
 * Created by liusheng on 17-10-12.
 */
public class IntArrayTermIndex<R> extends AbstractTermIndex<IntArray, R> {

    protected R add(IntArray value, R record) {
        return this.index.put(value, record);
    }

    protected boolean contains(IntArray value) {
        return this.index.containsKey(value);
    }

    protected R get(IntArray value) {
        return this.index.get(value);
    }

    protected R delete(IntArray value) {
        return this.index.remove(value);
    }

}

class IntArray {
    private Integer[] arr;

    public IntArray(Integer[] arr) {
        this.arr = arr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntArray intArray = (IntArray) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(arr, intArray.arr);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(arr);
    }

    public Integer[] getArr() {
        return arr;
    }

    public void setArr(Integer[] arr) {
        this.arr = arr;
    }
}
