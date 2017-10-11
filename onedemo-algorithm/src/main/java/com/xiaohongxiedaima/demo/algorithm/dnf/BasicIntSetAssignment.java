package com.xiaohongxiedaima.demo.algorithm.dnf;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by liusheng on 17-9-28.
 */
public class BasicIntSetAssignment extends AbstractAssignment<Set<Integer>, Integer> {
    public BasicIntSetAssignment(String label, Operator operator, Set<Integer> value) {
        super(label, operator, value);
    }

    protected Set<AbstractTerm<Integer>> createTerm() {
        Set<AbstractTerm<Integer>> termSet = new HashSet<AbstractTerm<Integer>>();
        for (Integer v : this.value) {
            termSet.add(new IntTerm(this.label, v));
        }
        return termSet;
    }
}
