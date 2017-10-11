package com.xiaohongxiedaima.demo.algorithm.dnf;

import java.util.Map;
import java.util.Set;

/**
 * Created by liusheng on 17-9-29.
 */
public abstract class AbstractTerm<T> {

    protected String name;
    protected T value;

    public AbstractTerm(String name, T value) {
        this.name = name;
        this.value = value;
    }

    abstract protected Set<Conjunction> match(T value, Map<Operator, Set<Conjunction>> conjunctions);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractTerm<?> that = (AbstractTerm<?>) o;

        if (!name.equals(that.name)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
