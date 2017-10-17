package com.xiaohongxiedaima.demo.algorithm.dnf.index.assignment;

import com.xiaohongxiedaima.demo.algorithm.dnf.index.Operator;
import com.xiaohongxiedaima.demo.algorithm.dnf.index.term.AbstractTerm;
import com.xiaohongxiedaima.demo.algorithm.dnf.index.term.StringTerm;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by liusheng on 17-9-28.
 */
public class BasicStringSetAssignment extends AbstractAssignment<Set<String>, String> {
    public BasicStringSetAssignment(String label, Operator operator, Set<String> value) {
        super(label, operator, value);
    }

    @Override
    public Set<AbstractTerm<String>> createTerm() {
        Set<AbstractTerm<String>> termSet = new HashSet<AbstractTerm<String>>();
        for (String v : this.value) {
            AbstractTerm<String> term = new StringTerm(this.label, v);
            termSet.add(term);
        }
        return termSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicStringSetAssignment that = (BasicStringSetAssignment)o;

        if (!this.label.equals(that.label)) return false;
        if (!this.operator.equals(that.operator)) return false;

        if (this.value.size() != that.value.size()) return false;

        for (String v : this.value) {
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
        for (String v : value) {
            valueResult = v.hashCode() + valueResult;
        }

        result = 31 * result + valueResult;
        return result;
    }
}
