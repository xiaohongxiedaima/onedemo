package com.xiaohongxiedaima.demo.algorithm.dnf.index.assignment;

import com.xiaohongxiedaima.demo.algorithm.dnf.index.Operator;
import com.xiaohongxiedaima.demo.algorithm.dnf.index.term.AbstractTerm;
import com.xiaohongxiedaima.demo.algorithm.dnf.index.term.IntArrayTerm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

;

/**
 * Created by liusheng on 17-9-28.
 */
public class BasicIntTowDimensionListAssignment extends AbstractAssignment<List<List<Integer>>, Integer[]> {
    public BasicIntTowDimensionListAssignment(String label, Operator operator, List<List<Integer>> value) {
        super(label, operator, value);
    }

    public Set<AbstractTerm<Integer[]>> createTerm() {
        Set<AbstractTerm<Integer[]>> set = new HashSet<AbstractTerm<Integer[]>>();
        for (int i = 0; i < this.value.size(); i ++) {
            List<Integer> subList = this.value.get(i);
            for (int j = 0; j < subList.size(); j ++) {
                Integer[] termValue = new Integer[]{
                        i + 1, subList.get(j)
                };
                AbstractTerm<Integer[]> term = new IntArrayTerm(this.label, termValue);
                set.add(term);
            }
        }
        return set;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicIntTowDimensionListAssignment that = (BasicIntTowDimensionListAssignment)o;

        if (!this.label.equals(that.label)) return false;
        if (!this.operator.equals(that.operator)) return false;

        if (this.value.size() != that.value.size()) return false;
        for (int i = 0; i < this.value.size(); i ++) {
            if (this.value.get(i).size() != that.value.get(i).size()) return false;
            for (int j = 0; j < this.value.get(i).size(); j ++) {
                if (this.value.get(i).get(j) != that.value.get(i).get(j)) return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {

        int result = label.hashCode();
        result = 31 * result + operator.hashCode();

        int valueResult = 0;
        for (int i = 0; i < this.value.size(); i ++) {
            int vResult = 0;
            for (int j = 0; j < this.value.get(i).size(); j ++) {
                vResult = vResult + this.value.get(i).get(j).hashCode();
            }
            valueResult = 31 * valueResult + vResult;
        }

        result = 31 * result + valueResult;
        return result;

    }
}
