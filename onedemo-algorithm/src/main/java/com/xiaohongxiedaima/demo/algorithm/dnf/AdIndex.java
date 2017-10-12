package com.xiaohongxiedaima.demo.algorithm.dnf;

import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdGroup;
import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdPlan;
import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdUnit;

import java.util.*;

import static com.xiaohongxiedaima.demo.algorithm.dnf.Operator.EQ;
import static com.xiaohongxiedaima.demo.algorithm.dnf.Operator.NE;

/**
 * Created by liusheng on 17-10-11.
 */
public class AdIndex {
    public static Map<String, Set<AbstractAssignment>> createAssignment(AdUnit adUnit, AdGroup adGroup, AdPlan adPlan) {
        // 广告计划
        Set<AbstractAssignment> planAssignments = new HashSet<AbstractAssignment>();
        planAssignments.add(new BasicIntAssignment("pStarttime", Operator.LE, adPlan.getStarttime()));
        Integer endtime = adPlan.getEndtime();
        if (endtime == 0) {
            endtime = Integer.MAX_VALUE;
        }
        planAssignments.add(new BasicIntAssignment("pEndtime", Operator.GE, endtime));

        // 广告组
        Set<AbstractAssignment> groupAssignments = new HashSet<AbstractAssignment>();
        if (adGroup.getAdType() != null && adGroup.getAdType().size() > 0) {
            Set<Integer> set = new HashSet<Integer>();
            set.addAll(adGroup.getAdType());
            groupAssignments.add(new BasicIntSetAssignment("gAdType", EQ, set));
        }
        if (adGroup.getRegion() != null && adGroup.getRegion().size() > 0) {
            Set<String> set = new HashSet<String>();
            set.addAll(adGroup.getRegion());
            groupAssignments.add(new BasicStringSetAssignment("gRegion", EQ, set));
        }
        if (adGroup.getSiteBlackList() != null && adGroup.getSiteBlackList().size() > 0) {
            Set<String> set = new HashSet<String>();
            set.addAll(adGroup.getSiteBlackList());
            groupAssignments.add(new BasicStringSetAssignment("gSiteBlackList", NE, set));
        }
        if (adGroup.getSiteWhiteList() != null && adGroup.getSiteWhiteList().size() > 0) {
            Set<String> set = new HashSet<String>();
            set.addAll(adGroup.getSiteWhiteList());
            groupAssignments.add(new BasicStringSetAssignment("gSiteWhiteList", EQ, set));
        }
        // schedule
        if (adGroup.getSchedule() != null && adGroup.getSchedule().size() > 0) {
            groupAssignments.add(new BasicIntTowDimensionListAssignment("gSchedule", EQ, adGroup.getSchedule()));
        }
        if (adGroup.getAdZoneId() != null && adGroup.getAdZoneId().size() > 0) {
            Set<String> set = new HashSet<String>();
            set.addAll(adGroup.getAdZoneId());
            groupAssignments.add(new BasicStringSetAssignment("gAdZoneId", NE, set));
        }

        if (adGroup.getEquipmentType() != null && adGroup.getEquipmentType().size() > 0) {
            for (List<String> cate4List : adGroup.getEquipmentType()) {
                String cate3 = MetaData.CATE_4_MAP.get(cate4List.get(0));

                Set<String> set = new HashSet<String>();
                set.addAll(cate4List);

                AbstractAssignment assignment = new BasicStringSetAssignment(cate3, EQ, set);
                groupAssignments.add(assignment);
            }
        }

        // 广告单元
        Set<AbstractAssignment> unitAssignments = new HashSet<AbstractAssignment>();
        unitAssignments.add(new BasicIntAssignment("uType", EQ, adUnit.getType()));
        if (adUnit.getWidth() != null) {
            unitAssignments.add(new BasicIntAssignment("uWidth", EQ, adUnit.getWidth()));
        }
        if (adUnit.getHeight() != null) {
            unitAssignments.add(new BasicIntAssignment("uHeight", EQ, adUnit.getHeight()));
        }

        // 人群标签
        Set<AbstractAssignment> crowdAssignments = new HashSet<AbstractAssignment>();
        if (adGroup.getCrowd() != null && adGroup.getCrowd().size() > 0) {
            for (List<String> cate4List : adGroup.getCrowd()) {
                String cate3 = MetaData.CATE_4_MAP.get(cate4List.get(0));

                Set<String> set = new HashSet<String>();
                set.addAll(cate4List);

                AbstractAssignment assignment = new BasicStringSetAssignment(cate3, EQ, set);
                crowdAssignments.add(assignment);
            }
        }

        // 关键字
//        Set<AbstractAssignment> keywordAssignments = Collections.emptySet();
//        if (adGroup.getKeyword() != null && adGroup.getKeyword().size() > 0) {
//            for (String kw : adGroup.getKeyword()) {
//                keywordAssignments.add(new ValueAssignment(kw));
//            }
//        }

        // 上下文标签
//        Set<AbstractAssignment> contentTagAssignments = Collections.emptySet();
//        if (adGroup.getContentTag() != null && adGroup.getContentTag().size() > 0) {
//            for (String tag : adGroup.getContentTag()) {
//                contentTagAssignments.add(new ValueAssignment(tag));
//            }
//        }

        Map<String, Set<AbstractAssignment>> assignmentMap = new HashMap<String, Set<AbstractAssignment>>();
        assignmentMap.put("plan", planAssignments);
        assignmentMap.put("group", groupAssignments);
        assignmentMap.put("unit", unitAssignments);
        assignmentMap.put("crowd", crowdAssignments);
//        assignmentMap.put("keyword", keywordAssignments);
//        assignmentMap.put("contentTag", contentTagAssignments);

        return assignmentMap;
    }
}
