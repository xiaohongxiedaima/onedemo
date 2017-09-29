package com.xiaohongxiedaima.demo.algorithm.dnf;

import java.util.Set;

/**
 * Created by liusheng on 17-9-28.
 */
public class BasicStringSetAssignment extends AbstractAssignment<Set<String>, String> {
    public BasicStringSetAssignment(String label, Operator operator, Set<String> value) {
        super(label, operator, value);
    }

    protected Boolean match(String value) {
        switch (this.operator) {
            case IN:
                return this.value.contains(value);
            case NI:
                return !this.value.contains(value);
        }
        return false;
    }
}
