package com.xiaohongxiedaima.demo.algorithm.dnf;

/**
 * Created by liusheng on 17-9-28.
 */
public class ValueAssignment extends AbstractAssignment<String, String> {
    public ValueAssignment(String value) {
        super(null, null, value);
    }

    protected Boolean match(String value) {
        return this.value.equals(value);
    }
}
