package com.xiaohongxiedaima.demo.algorithm.dnf.index.assignment;


import com.xiaohongxiedaima.demo.algorithm.dnf.index.Operator;
import com.xiaohongxiedaima.demo.algorithm.dnf.index.term.AbstractTerm;
import com.xiaohongxiedaima.demo.algorithm.dnf.index.term.IntTerm;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by liusheng on 17-9-28.
 */
public class BasicIntSetAssignment extends AbstractAssignment<Set<Integer>, Integer> {
    public BasicIntSetAssignment(String label, Operator operator, Set<Integer> value) {
        super(label, operator, value);
    }

    public Set<AbstractTerm<Integer>> createTerm() {
        Set<AbstractTerm<Integer>> termSet = new HashSet<AbstractTerm<Integer>>();
        for (Integer v : this.value) {
            termSet.add(new IntTerm(this.label, v));
        }
        return termSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicIntSetAssignment that = (BasicIntSetAssignment)o;

        if (!this.label.equals(that.label)) return false;
        if (!this.operator.equals(that.operator)) return false;

        if (this.value.size() != that.value.size()) return false;

        for (Integer v : this.value) {
            if (!that.value.contains(v)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = label.hashCode();
        result = 31 * result + operator.hashCode();

        int valueResult = 0;
        for (Integer v : value) {
            valueResult = v.hashCode() + valueResult;
        }

        result = 31 * result + valueResult;
        return result;
    }
}
