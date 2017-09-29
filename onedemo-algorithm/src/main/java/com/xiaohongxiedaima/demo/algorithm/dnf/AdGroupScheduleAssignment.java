package com.xiaohongxiedaima.demo.algorithm.dnf;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by liusheng on 17-9-28.
 */
public class AdGroupScheduleAssignment extends AbstractAssignment<Map<Integer, Set<Integer>>, Integer[]> {
    public AdGroupScheduleAssignment(String label, Map<Integer, Set<Integer>> value) {
        super(label, null, value);
    }

    public AdGroupScheduleAssignment(String label, Operator operator, Map<Integer, Set<Integer>> value) {
        super(label, operator, value);
    }

    protected Boolean match(Integer[] value) {
        if (this.value.containsKey(value[0])) {
            Set<Integer> timeSet = this.value.get(value[0]);
            if (timeSet.contains(value[1])) {
                return true;
            }
        }
        return false;
    }


}
