package com.xiaohongxiedaima.demo.algorithm.dnf;

/**
 * Created by liusheng on 17-9-28.
 */
public class BasicIntAssignment  extends AbstractAssignment<Integer, Integer>{

    public BasicIntAssignment(String label, Operator operator, Integer value) {
        super(label, operator, value);
    }

    protected Boolean match(Integer value) {
        switch (this.operator) {
            case GE:
                return this.value >= value;
            case GT:
                return this.value > value;
            case EQ:
                return this.value == value;
            case LE:
                return this.value <= value;
            case LT:
                return this.value < value;
        }
        return false;
    }

}
