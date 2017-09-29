package com.xiaohongxiedaima.demo.algorithm.dnf;

import java.util.Set;

/**
 * Created by liusheng on 17-9-28.
 */
public class BasicStringSetInSetAssignment extends AbstractAssignment<Set<String>, Set<String>> {
    public BasicStringSetInSetAssignment(String label, Operator operator, Set<String> value) {
        super(label, operator, value);
    }

    protected Boolean match(Set<String> value) {
        switch (this.operator) {
            case OI:
                for (String v : value) {
                    if (this.value.contains(v)) return true;
                }
        }
        return false;
    }
}
