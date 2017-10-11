package com.xiaohongxiedaima.demo.algorithm.dnf;

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
    protected Set<AbstractTerm<String>> createTerm() {
        Set<AbstractTerm<String>> termSet = new HashSet<AbstractTerm<String>>();
        for (String v : this.value) {
            AbstractTerm<String> term = new StringTerm(this.label, v);
            termSet.add(term);
        }
        return termSet;
    }
}
