package com.xiaohongxiedaima.demo.algorithm.dnf;

import com.xiaohongxiedaima.demo.algorithm.dnf.index.Conjunction;
import com.xiaohongxiedaima.demo.algorithm.dnf.index.Operator;
import com.xiaohongxiedaima.demo.algorithm.dnf.index.assignment.AbstractAssignment;
import com.xiaohongxiedaima.demo.algorithm.dnf.index.assignment.BasicIntAssignment;
import com.xiaohongxiedaima.demo.algorithm.dnf.index.assignment.BasicIntTowDimensionListAssignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by liusheng on 17-10-12.
 */
public class ConjunctionTest {

    private static final Logger LOG = LoggerFactory.getLogger(ConjunctionTest.class);

    @Test
    public void testAddToSet() {

        AbstractAssignment a1 = new BasicIntAssignment("pStarttime", Operator.EQ, 1471795200);
        List<List<Integer>> schedule = Arrays.asList(Arrays.asList(0, 1, 2));
        AbstractAssignment a2 = new BasicIntTowDimensionListAssignment("gSchedule", Operator.EQ, schedule);

        Set<AbstractAssignment> set = new HashSet<AbstractAssignment>();
        set.add(a1);
        set.add(a2);
        Conjunction c1 = new Conjunction(set);

        AbstractAssignment a3 = new BasicIntAssignment("pStarttime", Operator.EQ, 1471795200);
        schedule = Arrays.asList(Arrays.asList(0, 1, 2));
        AbstractAssignment a4 = new BasicIntTowDimensionListAssignment("gSchedule", Operator.EQ, schedule);

        set = new HashSet<AbstractAssignment>();
        set.add(a3);
        set.add(a4);

        Conjunction c2 = new Conjunction(set);

        Set<Conjunction> conjunctionSet = new HashSet<Conjunction>();
        conjunctionSet.add(c1);
        conjunctionSet.add(c2);

        LOG.info("{}, {}, {}", a1.hashCode(), a2.hashCode(), c1.hashCode());
        LOG.info("{}, {}, {}", a3.hashCode(), a4.hashCode(), c2.hashCode());

        LOG.info("{}, {}, {}", a1.equals(a3), a2.equals(a4), c1.equals(c2));

        LOG.info("{}", conjunctionSet.size());

    }

}
