package com.xiaohongxiedaima.demo.algorithm.dnf;

/**
 * Created by liusheng on 17-9-26.
 */
public abstract class AbstractAssignment<T1, T2> {
    protected String label;
    protected Operator operator;
    protected T1 value;

    public AbstractAssignment(String label, Operator operator, T1 value) {
        this.label = label;
        this.operator = operator;
        this.value = value;
    }

    protected abstract Boolean match(T2 value);

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public T1 getValue() {
        return value;
    }

    public void setValue(T1 value) {
        this.value = value;
    }
}
