package com.xiaohongxiedaima.demo.algorithm.dnf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdGroup;
import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdPlan;
import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.xiaohongxiedaima.demo.algorithm.dnf.Operator.EQ;
import static com.xiaohongxiedaima.demo.algorithm.dnf.Operator.NE;

/**
 * Created by liusheng on 17-9-28.
 */
public class InvertedIndex {

    private static final Logger LOG = LoggerFactory.getLogger(InvertedIndex.class);

    private Map<Conjunction, Set<String>> conjunctions = new HashMap<Conjunction, Set<String>>();

    private Map<Integer, Map<AbstractAssignment, Set<Conjunction>>> assignmentSize =
            new TreeMap<Integer, Map<AbstractAssignment, Set<Conjunction>>>();

    private Map<Integer, Map<AbstractTerm, Map<Operator, Set<Conjunction>>>> layeredTermMap =
            new HashMap<Integer, Map<AbstractTerm, Map<Operator, Set<Conjunction>>>>();

    private Map<String, Set<AbstractAssignment>> createAssignment(AdUnit adUnit, AdGroup adGroup, AdPlan adPlan) {
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

    /**
     * @param assignments Set<> 表示一个conjunction的所有assignment list标示多个conjunction
     * @param id
     */
    private void add(List<Set<AbstractAssignment>> assignments, String id) {
        for (Set<AbstractAssignment> set : assignments) {
            Conjunction conjunction = new Conjunction(set);

            // 一层索引
            Set<String> ids = conjunctions.get(conjunction);
            if (ids == null) {
                ids = new HashSet<String>();
                conjunctions.put(conjunction, ids);
            }
            ids.add(id);

            // 二层索引
            Integer layer = set.size();
            Map<AbstractTerm, Map<Operator, Set<Conjunction>>> termMap = layeredTermMap.get(layer);

            if (termMap == null) {
                termMap = new HashMap<AbstractTerm, Map<Operator, Set<Conjunction>>>();
                layeredTermMap.put(layer, termMap);
            }

            for (AbstractAssignment assignment : set) {
                Set<AbstractTerm> termSet = assignment.createTerm();
                for (AbstractTerm term : termSet) {
                    Map<Operator, Set<Conjunction>> operatorConjunctionMap = termMap.get(term);

                    if (operatorConjunctionMap == null) {
                        operatorConjunctionMap = new HashMap<Operator, Set<Conjunction>>();
                        termMap.put(term, operatorConjunctionMap);
                    }

                    Set<Conjunction> cSet = operatorConjunctionMap.get(assignment.operator);
                    if (cSet == null) {
                        cSet = new HashSet<Conjunction>();
                        operatorConjunctionMap.put(assignment.operator, cSet);
                    }
                    cSet.add(conjunction);
                }
            }
        }
    }

    private Set<String> query(Map<String, Object> terms) {
        Integer maxLayer = terms.size();

        Set<String> ids = new HashSet<String>();

        for (Map.Entry<Integer, Map<AbstractTerm, Map<Operator, Set<Conjunction>>>> entry : layeredTermMap.entrySet()) {
            Integer currentLayer = entry.getKey();

            if (currentLayer > maxLayer) break;

            Map<Conjunction, Integer> matchCountMap = new HashMap<Conjunction, Integer>();

            for (Map.Entry<AbstractTerm, Map<Operator, Set<Conjunction>>> termAndConjunction : entry.getValue().entrySet()) {
                AbstractTerm term = termAndConjunction.getKey();
                String name = term.getName();
                if (terms.containsKey(name)) {
                    Set<Conjunction> matchSet = term.match(terms.get(name), termAndConjunction.getValue());
                    for (Conjunction matchC : matchSet) {
                        Integer count = matchCountMap.get(matchC);
                        if (count == null) {
                            count = 1;
                        } else {
                            count ++;
                        }
                        matchCountMap.put(matchC, count);
                    }
                }
            }

            for (Map.Entry<Conjunction, Integer> cCountEntry : matchCountMap.entrySet()) {
                if (cCountEntry.getValue() == currentLayer) {
                    ids.addAll(conjunctions.get(cCountEntry.getKey()));
                }
            }
        }

        return ids;
    }

    public static void main(String[] args) {
        InvertedIndex index = new InvertedIndex();

        Map<String, AdPlan> adPlanMap = MetaData.AD_PLAN_MAP;
        Map<String, AdGroup> adGroupMap = MetaData.AD_GROUP_MAP;
        Map<String, AdUnit> adUnitMap = MetaData.AD_UNIT_MAP;

        Integer count = 0;
        for (Map.Entry<String, AdUnit> entry : adUnitMap.entrySet()) {
//            if (count ++ > 1000) break;
            String id = entry.getKey();
            AdUnit adUnit = entry.getValue();
            AdGroup adGroup = adGroupMap.get(adUnit.getGroupId());
            if (adGroup == null) continue;
            AdPlan adPlan = adPlanMap.get(adGroup.getPlanId());
            if (adPlan == null) continue;
            Map<String, Set<AbstractAssignment>> assignmentMap = index.createAssignment(adUnit, adGroup, adPlan);

            Set<AbstractAssignment> planAssignments = assignmentMap.get("plan");
            Set<AbstractAssignment> groupAssignments = assignmentMap.get("group");
            Set<AbstractAssignment> unitAssignments = assignmentMap.get("unit");

            Set<AbstractAssignment> crowdAssignments = assignmentMap.get("crowd");

            Set<AbstractAssignment> assignments = new HashSet<AbstractAssignment>();
            assignments.addAll(planAssignments);
            assignments.addAll(groupAssignments);
            assignments.addAll(unitAssignments);
            assignments.addAll(crowdAssignments);
            List<Set<AbstractAssignment>> conjunctionList = Arrays.asList(assignments);

            index.add(conjunctionList, id);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        LOG.info(gson.toJson(index.conjunctions));
//        LOG.info(gson.toJson(index.layeredTermMap));

        // query test

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
        queryMap.put("uType", 2);

        Set<String> ids = index.query(queryMap);

        LOG.info(gson.toJson(ids));
    }

}
