package com.xiaohongxiedaima.demo.algorithm.dnf;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by liusheng on 17-9-30.
 */
public class IntArrayTerm extends AbstractTerm<Integer[]> {
    public IntArrayTerm(String name, Integer[] value) {
        super(name, value);
    }

    protected Set<Conjunction> match(Integer[] value, Map<Operator, Set<Conjunction>> conjunctions) {
        Set<Conjunction> set = new HashSet<Conjunction>();
        for (Map.Entry<Operator, Set<Conjunction>> entry : conjunctions.entrySet()) {
            switch (entry.getKey()) {
                case EQ:
                    if (this.value[0] == value[0] && this.value[1] == value[1]) {
                        set.addAll(entry.getValue());
                    }
            }
        }

        return set;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntArrayTerm that = (IntArrayTerm) o;

        if (!name.equals(that.name)) return false;

        if (this.value[0] != value[0]) return false;

        if (this.value[1] != value[1]) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + value[0].hashCode() + value[1].hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("(");
        sb.append(this.name).append(",").append("[").append(this.value[0]).append(",").append(this.value[1]).append("]");
        sb.append(")");
        return sb.toString();
    }
}
