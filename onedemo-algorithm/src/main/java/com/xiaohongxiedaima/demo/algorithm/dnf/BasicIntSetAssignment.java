package com.xiaohongxiedaima.demo.algorithm.dnf;

import java.util.Set;

/**
 * Created by liusheng on 17-9-28.
 */
public class BasicIntSetAssignment extends AbstractAssignment<Set<Integer>, Integer> {
    public BasicIntSetAssignment(String label, Operator operator, Set<Integer> value) {
        super(label, operator, value);
    }

    protected Boolean match(Integer value) {
        switch (this.operator) {
            case IN:
                return this.value.contains(value);
            case NI:
                return !this.value.contains(value);
        }
        return false;
    }
}
