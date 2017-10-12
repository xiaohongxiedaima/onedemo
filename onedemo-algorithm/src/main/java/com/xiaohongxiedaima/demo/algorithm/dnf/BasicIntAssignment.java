package com.xiaohongxiedaima.demo.algorithm.dnf;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by liusheng on 17-9-28.
 */
public class BasicIntAssignment  extends AbstractAssignment<Integer, Integer>{

    public BasicIntAssignment(String label, Operator operator, Integer value) {
        super(label, operator, value);
    }

    protected Set<AbstractTerm<Integer>> createTerm() {
        IntTerm term = new IntTerm(this.label, this.value);
        Set<AbstractTerm<Integer>> set = new HashSet<AbstractTerm<Integer>>();
        set.add(term);
        return set;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicIntAssignment that = (BasicIntAssignment)o;

        if (!this.label.equals(that.label)) return false;
        if (!this.operator.equals(that.operator)) return false;
        if (!this.value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = label.hashCode();
        result = 31 * result + operator.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

}
