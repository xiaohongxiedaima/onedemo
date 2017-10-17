package com.xiaohongxiedaima.demo.algorithm.dnf;

import com.xiaohongxiedaima.demo.algorithm.dnf.index.Conjunction;
import com.xiaohongxiedaima.demo.algorithm.dnf.index.InvertedIndex;
import com.xiaohongxiedaima.demo.algorithm.dnf.index.assignment.*;
import com.xiaohongxiedaima.demo.algorithm.dnf.index.term.AbstractTerm;
import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdGroup;
import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdPlan;
import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdUnit;

import java.util.*;

import static com.xiaohongxiedaima.demo.algorithm.dnf.index.Operator.EQ;
import static com.xiaohongxiedaima.demo.algorithm.dnf.index.Operator.NE;

/**
 * Created by liusheng on 17-10-11.
 */
public class AdIndex {

    private static InvertedIndex crowdIndex = new InvertedIndex();
    private static InvertedIndex contentTagIndex = new InvertedIndex();

    public static void init() {
        AdMetaData.loadAd();
        AdMetaData.loadUserLabelMapping();
        AdMetaData.loadTopicMapping();
        build();
    }

    private static void build() {
        Map<String, AdPlan> adPlanMap = AdMetaData.AD_PLAN_MAP;
        Map<String, AdGroup> adGroupMap = AdMetaData.AD_GROUP_MAP;
        Map<String, AdUnit> adUnitMap = AdMetaData.AD_UNIT_MAP;

        for (Map.Entry<String, AdUnit> entry : adUnitMap.entrySet()) {
            AdUnit adUnit = entry.getValue();
            AdGroup adGroup = adGroupMap.get(adUnit.getGroupId());
            if (adGroup == null) continue;
            AdPlan adPlan = adPlanMap.get(adGroup.getPlanId());
            if (adPlan == null) continue;
            add(adUnit, adGroup, adPlan);
        }
    }

    public static void add(AdUnit adUnit, AdGroup adGroup, AdPlan adPlan) {
        String id = adUnit.getId();

        Map<String, Set<AbstractAssignment>> assignmentMap = AdIndex.createAssignment(adUnit, adGroup, adPlan);

        Set<AbstractAssignment> planAssignments = assignmentMap.get("plan");
        Set<AbstractAssignment> groupAssignments = assignmentMap.get("group");
        Set<AbstractAssignment> unitAssignments = assignmentMap.get("unit");
        Set<AbstractAssignment> crowdAssignments = assignmentMap.get("crowd");
        Set<AbstractAssignment> keywordAssignments = assignmentMap.get("keyword");
        Set<AbstractAssignment> contentTagAssignments = assignmentMap.get("contentTag");

        Set<AbstractAssignment> assignments = new HashSet<AbstractAssignment>();
        assignments.addAll(planAssignments);
        assignments.addAll(groupAssignments);
        assignments.addAll(unitAssignments);
        assignments.addAll(crowdAssignments);

        if (crowdAssignments.size() > 0) {
            assignments.addAll(crowdAssignments);
            Conjunction conjunction = new Conjunction(assignments);
            List<Conjunction> conjunctionList = Arrays.asList(conjunction);
            crowdIndex.add(conjunctionList, id);
        } else {
            List<Conjunction> conjunctionList = new ArrayList<Conjunction>();
            conjunctionList.add(new Conjunction(assignments));

            Set<AbstractAssignment> copyAssignments1 = new HashSet<AbstractAssignment>();
            Set<AbstractAssignment> copyAssignments2 = new HashSet<AbstractAssignment>();
            for (AbstractAssignment assignment : assignments) {
                copyAssignments1.add(assignment);
                copyAssignments2.add(assignment);
            }
            if (keywordAssignments.size() > 0) {
                copyAssignments1.addAll(keywordAssignments);
                conjunctionList.add(new Conjunction(copyAssignments1));
            }

            if (contentTagAssignments.size() > 0) {
                copyAssignments2.addAll(contentTagAssignments);
                conjunctionList.add(new Conjunction(copyAssignments2));
            }

            contentTagIndex.add(conjunctionList, id);
        }
    }

    public static void remove(Set<String> ids) {
        crowdIndex.remove(ids);
        contentTagIndex.remove(ids);
    }

    public static Set<String> query(Set<AbstractTerm> queryTerms, Integer type) {
        Set<String> set = new HashSet<String>();
        if (type == 1) {
            set = crowdIndex.query(queryTerms);
        }
        if (type == 2) {
            set = contentTagIndex.query(queryTerms);
        }
        return set;
    }

    public static Map<String, Set<AbstractAssignment>> createAssignment(AdUnit adUnit, AdGroup adGroup, AdPlan adPlan) {

        Set<AbstractAssignment> planAssignments = new HashSet<AbstractAssignment>();

        if (adPlan.getBudget() > 0) {
            planAssignments.add(new BasicIntAssignment("pBudget", EQ, 1));
        } else {
            planAssignments.add(new BasicIntAssignment("pBudget", EQ, 0));
        }
        planAssignments.add(new BasicIntAssignment("pStatus", EQ, adPlan.getStatus()));
        planAssignments.add(new BasicIntAssignment("pTodayStatus", EQ, adPlan.getTodayStatus()));

        // 广告组
        Set<AbstractAssignment> groupAssignments = new HashSet<AbstractAssignment>();
        groupAssignments.add(new BasicIntAssignment("gPlanStatus", EQ, adGroup.getPlanStatus()));
        groupAssignments.add(new BasicIntAssignment("gStatus", EQ, adGroup.getStatus()));

        if (adGroup.getAdType() != null && adGroup.getAdType().size() > 0) {
            Set<Integer> set = new HashSet<Integer>();
            set.addAll(adGroup.getAdType());
            groupAssignments.add(new BasicIntSetAssignment("gAdType", EQ, set));
        }
        // TODO region 规则暂时不清楚
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
            groupAssignments.add(new BasicStringSetAssignment("gAdZoneId", EQ, set));
        }

        if (adGroup.getEquipmentType() != null && adGroup.getEquipmentType().size() > 0) {
            for (List<String> cate4List : adGroup.getEquipmentType()) {
                groupAssignments.add(createAssignmentFormCate4List(cate4List));
            }
        }

        // 广告单元
        Set<AbstractAssignment> unitAssignments = new HashSet<AbstractAssignment>();
        unitAssignments.add(new BasicIntAssignment("uStatus", EQ, adUnit.getStatus()));
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
                crowdAssignments.add(createAssignmentFormCate4List(cate4List));
            }
        }

        // 关键字
        Set<AbstractAssignment> keywordAssignments = new HashSet<AbstractAssignment>();
        if (adGroup.getKeyword() != null && adGroup.getKeyword().size() > 0) {
            Set<String> set = new HashSet<String>();
            set.addAll(adGroup.getKeyword());
            keywordAssignments.add(new BasicStringSetAssignment("keyword", EQ, set));
        }

        // 上下文标签
        Set<AbstractAssignment> contentTagAssignments = new HashSet<AbstractAssignment>();
        if (adGroup.getContentTag() != null && adGroup.getContentTag().size() > 0) {
            Map<String, Set<String>> map = new HashMap<String, Set<String>>();
            for (String contentTag : adGroup.getContentTag()) {
                String cate1 = AdMetaData.TOPIC_2_1_MAPPING.get(contentTag);
                Set<String> set = map.get(cate1);
                if (set == null) {
                    set = new HashSet<String>();
                    map.put(cate1, set);
                }
                set.add(contentTag);
            }
            for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
                contentTagAssignments.add(new BasicStringSetAssignment(entry.getKey(), EQ, entry.getValue()));
            }
        }

        Map<String, Set<AbstractAssignment>> assignmentMap = new HashMap<String, Set<AbstractAssignment>>();
        assignmentMap.put("plan", planAssignments);
        assignmentMap.put("group", groupAssignments);
        assignmentMap.put("unit", unitAssignments);
        assignmentMap.put("crowd", crowdAssignments);
        assignmentMap.put("keyword", keywordAssignments);
        assignmentMap.put("contentTag", contentTagAssignments);

        return assignmentMap;
    }

    private static AbstractAssignment createAssignmentFormCate4List(List<String> cate4List) {
        String cate3 = AdMetaData.CATE4_3_MAPPING.get(cate4List.get(0));

        Set<String> set = new HashSet<String>();
        set.addAll(cate4List);

        AbstractAssignment assignment = new BasicStringSetAssignment(cate3, EQ, set);
        return assignment;
    }
}
