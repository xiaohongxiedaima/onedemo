package com.xiaohongxiedaima.demo.algorithm.dnf;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by liusheng on 17-9-28.
 */
public class BasicIntTowDimensionListAssignment extends AbstractAssignment<List<List<Integer>>, Integer[]> {
    public BasicIntTowDimensionListAssignment(String label, Operator operator, List<List<Integer>> value) {
        super(label, operator, value);
    }

    protected Set<AbstractTerm<Integer[]>> createTerm() {
        Set<AbstractTerm<Integer[]>> set = new HashSet<AbstractTerm<Integer[]>>();
        for (int i = 0; i < this.value.size(); i ++) {
            List<Integer> subList = this.value.get(i);
            for (int j = 0; j < subList.size(); j ++) {
                Integer[] termValue = new Integer[]{
                        i + 1, subList.get(j)
                };
                AbstractTerm<Integer[]> term = new IntArrayTerm(this.label, termValue);
                set.add(term);
            }
        }
        return set;
    }


//    protected Boolean match(Integer[] value) {
//        if (this.value.containsKey(value[0])) {
//            Set<Integer> timeSet = this.value.get(value[0]);
//            if (timeSet.contains(value[1])) {
//                return true;
//            }
//        }
//        return false;
//    }



}
