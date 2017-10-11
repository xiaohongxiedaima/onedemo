package com.xiaohongxiedaima.demo.algorithm.dnf;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by liusheng on 17-9-29.
 */
public class IntTerm extends AbstractTerm<Integer> {
    public IntTerm(String name, Integer value) {
        super(name, value);
    }

    protected Set<Conjunction> match(Integer value, Map<Operator, Set<Conjunction>> conjunctions) {
        Set<Conjunction> set = new HashSet<Conjunction>();
        for (Map.Entry<Operator, Set<Conjunction>> entry : conjunctions.entrySet()) {
            switch (entry.getKey()) {
                case EQ:
                    if (this.value == value) set.addAll(entry.getValue());
                case NE:
                    if (this.value != value) set.addAll(entry.getValue());
                case LE:
                    if (this.value <= value) set.addAll(entry.getValue());
                case GE:
                    if (this.value >= value) set.addAll(entry.getValue());
            }
        }

        return set;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntTerm that = (IntTerm) o;

        if (!name.equals(that.name)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("(");
        sb.append(this.name).append(",").append(this.value);
        sb.append(")");
        return sb.toString();
    }
}
