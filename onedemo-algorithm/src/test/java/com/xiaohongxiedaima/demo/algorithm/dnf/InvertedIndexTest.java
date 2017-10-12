package com.xiaohongxiedaima.demo.algorithm.dnf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdGroup;
import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdPlan;
import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.*;

/**
 * Created by liusheng on 17-10-11.
 */
public class InvertedIndexTest {

    private static final Logger LOG = LoggerFactory.getLogger(InvertedIndexTest.class);

    static InvertedIndex index = new InvertedIndex();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @BeforeTest
    public void testLoad() {
        Map<String, AdPlan> adPlanMap = MetaData.AD_PLAN_MAP;
        Map<String, AdGroup> adGroupMap = MetaData.AD_GROUP_MAP;
        Map<String, AdUnit> adUnitMap = MetaData.AD_UNIT_MAP;

        Integer count = 0;
        for (Map.Entry<String, AdUnit> entry : adUnitMap.entrySet()) {
//            if (count ++ > 100) break;
            String id = entry.getKey();
            AdUnit adUnit = entry.getValue();
            AdGroup adGroup = adGroupMap.get(adUnit.getGroupId());
            if (adGroup == null) continue;
            AdPlan adPlan = adPlanMap.get(adGroup.getPlanId());
            if (adPlan == null) continue;
            Map<String, Set<AbstractAssignment>> assignmentMap = AdIndex.createAssignment(adUnit, adGroup, adPlan);

            Set<AbstractAssignment> planAssignments = assignmentMap.get("plan");
            Set<AbstractAssignment> groupAssignments = assignmentMap.get("group");
            Set<AbstractAssignment> unitAssignments = assignmentMap.get("unit");

            Set<AbstractAssignment> crowdAssignments = assignmentMap.get("crowd");

            Set<AbstractAssignment> assignments = new HashSet<AbstractAssignment>();
            assignments.addAll(planAssignments);
            assignments.addAll(groupAssignments);
            assignments.addAll(unitAssignments);
            assignments.addAll(crowdAssignments);

            Conjunction conjunction = new Conjunction(assignments);

            List<Conjunction> conjunctionList = Arrays.asList(conjunction);

            index.add(conjunctionList, id);
        }
    }

    @Test(invocationCount=2000,threadPoolSize=20)
    public void testRemove() {
        Set<String> removeIds = new HashSet<String>();
        removeIds.add("1ieiq6a12tde");
        removeIds.add("15ftby76cs1b");
        index.remove(removeIds);

//        LOG.info(gson.toJson(ids));
    }

    @Test(invocationCount=2000,threadPoolSize=20)
    public void testQuery() {
        Integer currentTimestamp = (int)(System.currentTimeMillis() / 1000);
        Map<String, Object> queryMap = new HashMap<String, Object>();
        // plan
        queryMap.put("pStarttime", currentTimestamp);
        queryMap.put("pEndtime", currentTimestamp);
        // group
        Calendar calendar = Calendar.getInstance();
        Integer week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (week == 0) week = 7;
        Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
        queryMap.put("gSchedule", new Integer[]{week, hour});

        queryMap.put("gAdType", 1);

        // unit
        queryMap.put("uWidth", 640);
        queryMap.put("uHeight", 260);
        queryMap.put("uType", 1);

        Set<String> ids = index.query(queryMap);

        LOG.info(gson.toJson(ids));
    }
}
