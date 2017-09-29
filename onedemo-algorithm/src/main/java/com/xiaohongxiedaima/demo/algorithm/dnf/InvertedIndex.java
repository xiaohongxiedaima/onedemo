package com.xiaohongxiedaima.demo.algorithm.dnf;

import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdGroup;
import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdPlan;
import com.xiaohongxiedaima.demo.algorithm.dnf.model.AdUnit;

import java.util.*;

import static com.xiaohongxiedaima.demo.algorithm.dnf.Operator.EQ;
import static com.xiaohongxiedaima.demo.algorithm.dnf.Operator.IN;

/**
 * Created by liusheng on 17-9-28.
 */
public class InvertedIndex {

    private Map<Conjunction, Set<String>> conjunctions = new HashMap<Conjunction, Set<String>>();

    private Map<Integer, Map<AbstractAssignment, Set<Conjunction>>> assignmentSize =
            new TreeMap<Integer, Map<AbstractAssignment, Set<Conjunction>>>();

    private Map<String, Set<AbstractAssignment>> createAssignment(AdUnit adUnit, AdGroup adGroup, AdPlan adPlan) {
        // 广告计划
        Set<AbstractAssignment> planAssignments = Collections.emptySet();
        planAssignments.add(new BasicIntAssignment("pStarttime", Operator.LE, adPlan.getStarttime()));
        planAssignments.add(new BasicIntAssignment("pEndtime", Operator.GE, adPlan.getEndtime()));

        // 广告组
        Set<AbstractAssignment> groupAssignments = Collections.emptySet();
        if (adGroup.getAdType() != null && adGroup.getAdType().size() > 0) {
            Set<Integer> set = Collections.emptySet();
            set.addAll(adGroup.getAdType());
            groupAssignments.add(new BasicIntSetAssignment("gAdType", IN, set));
        }
        if (adGroup.getRegion() != null && adGroup.getRegion().size() > 0) {
            Set<String> set = Collections.emptySet();
            set.addAll(adGroup.getRegion());
            groupAssignments.add(new BasicStringSetAssignment("gRegion", IN, set));
        }
        if (adGroup.getSiteBlackList() != null && adGroup.getSiteBlackList().size() > 0) {
            Set<String> set = Collections.emptySet();
            set.addAll(adGroup.getSiteBlackList());
            groupAssignments.add(new BasicStringSetAssignment("gSiteBlackList", IN, set));
        }
        if (adGroup.getSiteWhiteList() != null && adGroup.getSiteWhiteList().size() > 0) {
            Set<String> set = Collections.emptySet();
            set.addAll(adGroup.getSiteWhiteList());
            groupAssignments.add(new BasicStringSetAssignment("gSiteWhiteList", IN, set));
        }
        // schedule
        if (adGroup.getSchedule() != null && adGroup.getSchedule().size() > 0) {
            Map<Integer, Set<Integer>> map = new HashMap<Integer, Set<Integer>>();
            for (int i = 0; i < adGroup.getSchedule().size(); i++) {
                Set<Integer> set = Collections.emptySet();
                set.addAll(adGroup.getSchedule().get(i));
                map.put(i, set);
            }
            groupAssignments.add(new AdGroupScheduleAssignment("gSchedule", map));
        }
        if (adGroup.getAdZoneId() != null && adGroup.getAdZoneId().size() > 0) {
            Set<String> set = Collections.emptySet();
            set.addAll(adGroup.getAdZoneId());
            groupAssignments.add(new BasicStringSetAssignment("gAdZoneId", IN, set));
        }
//        if (adGroup.getEquipmentType() != null && adGroup.getEquipmentType().size() > 0) {
//            Set<String> set = Collections.emptySet();
//            set.addAll(adGroup.getEquipmentType());
//            groupAssignments.add(new BasicStringSetAssignment("gEquipmentType", IN, set));
//        }

        // 广告单元
        Set<AbstractAssignment> unitAssignments = Collections.emptySet();
        unitAssignments.add(new BasicIntAssignment("uType", EQ, adUnit.getType()));
        if (adUnit.getWidth() != null) {
            unitAssignments.add(new BasicIntAssignment("uWidth", EQ, adUnit.getWidth()));
        }
        if (adUnit.getHeight() != null) {
            unitAssignments.add(new BasicIntAssignment("uHeight", EQ, adUnit.getHeight()));
        }

        // 人群标签
        Set<AbstractAssignment> crowdAssignments = Collections.emptySet();
        if (adGroup.getCrowd() != null && adGroup.getCrowd().size() > 0) {
            for (List<String> level3 : adGroup.getCrowd()) {
                for (String label : level3) {
                    crowdAssignments.add(new ValueAssignment(label));
                }
            }
        }

        // 关键字
        Set<AbstractAssignment> keywordAssignments = Collections.emptySet();
        if (adGroup.getKeyword() != null && adGroup.getKeyword().size() > 0) {
            // 所有关键字做为一个 Assignment 处理
//            Set<String> set = Collections.emptySet();
//            set.addAll(adGroup.getKeyword());
//
//            keywordAssignments.add(new BasicStringSetInSetAssignment("keyword", OI, set));
            // 所有关键字拆为多个 Assignment 处理
            for (String kw : adGroup.getKeyword()) {
                keywordAssignments.add(new ValueAssignment(kw));
            }
        }

        // 上下文标签
        Set<AbstractAssignment> contentTagAssignments = Collections.emptySet();
        if (adGroup.getContentTag() != null && adGroup.getContentTag().size() > 0) {
            // 所有上下文定向拆为多个 Assignment 处理
            for (String tag : adGroup.getContentTag()) {
                contentTagAssignments.add(new ValueAssignment(tag));
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
            Integer size = conjunction.getAssignments().size();
            Map<AbstractAssignment, Set<Conjunction>> assignmentMap = assignmentSize.get(size);

            if (assignmentMap == null) {
                assignmentMap = new HashMap<AbstractAssignment, Set<Conjunction>>();
                assignmentSize.put(size, assignmentMap);
            }

            for (AbstractAssignment assignment : conjunction.getAssignments()) {
                Set<Conjunction> conjunctions = assignmentMap.get(assignment);
                if (conjunctions == null) {
                    conjunctions = new HashSet<Conjunction>();
                    assignmentMap.put(assignment, conjunctions);
                }
                conjunctions.add(conjunction);
            }
        }
    }

    private Set<String> query(Map<String, Object> noneValueAssignment, Set<String> valueAssignment) {

        Set<String> ids = new HashSet<String>();

        Integer size = noneValueAssignment.size() + valueAssignment.size();

        for (Map.Entry<Integer, Map<AbstractAssignment, Set<Conjunction>>> entry : this.assignmentSize.entrySet()) {
            if (size < entry.getKey()) {
                Map<Conjunction, Integer> matchCount = new HashMap<Conjunction, Integer>();
                for (Map.Entry<AbstractAssignment, Set<Conjunction>> assignmentMap : entry.getValue().entrySet()) {
                    AbstractAssignment assignment = assignmentMap.getKey();
                    Set<Conjunction> conjunctions = assignmentMap.getValue();

                    Boolean match = false;
                    if (assignment instanceof ValueAssignment) {
                        String value = ((ValueAssignment) assignment).getValue();
                        if (valueAssignment.contains(value)) {
                            match = true;
                        }
                    } else {
                        String label = assignment.getLabel();
                        Object queryValue = noneValueAssignment.get(label);
                        if (queryValue != null) {
                            match = assignment.match(queryValue);
                        }
                    }

                    if (match == true) {
                        for (Conjunction conjunction : conjunctions) {
                            Integer count = matchCount.get(conjunction);
                            if (count == null) {
                                count = 1;
                            } else {
                                count ++;
                            }
                            matchCount.put(conjunction, count);
                        }
                    }
                }

                // 计算是否有conjunction匹配
                Integer totalMatchConjunction = 0;
                for (Map.Entry<Conjunction, Integer> cCount : matchCount.entrySet()) {
                    if (cCount.getKey().getAssignments().size() == cCount.getValue()) {
                        totalMatchConjunction ++;
                        ids.addAll(conjunctions.get(cCount.getKey()));
                    }
                }
                if (totalMatchConjunction == 0) {
                    break;
                }
            }
        }

        return ids;
    }

    private void remove(String id) {
        for (Map.Entry<Conjunction, Set<String>> first : conjunctions.entrySet()) {
            for (Map.Entry<Integer, Map<AbstractAssignment, Set<Conjunction>>> second : assignmentSize.entrySet()) {
                Map<AbstractAssignment, Set<Conjunction>> assignmentMap = second.getValue();
                // 删除第二层
                for (Map.Entry<AbstractAssignment, Set<Conjunction>> entry : assignmentMap.entrySet()) {
                    if (entry.getValue().contains(first.getKey())) {
                        entry.getValue().remove(first.getKey());
                    }
                    if (entry.getValue().size() == 0) {
                        assignmentMap.remove(entry.getKey());
                    }
                }
                if (assignmentMap.size() == 0) {
                    assignmentSize.remove(second.getKey());
                }
            }
        }
    }

    public static void main(String[] args) {

//        Map<String, Set<AbstractAssignment>> assignmentMap = createAssignment(adUnit, adGroup, adPlan);
//
//        Set<AbstractAssignment> planAssignments = assignmentMap.get("plan");
//        Set<AbstractAssignment> groupAssignments = assignmentMap.get("group");
//        Set<AbstractAssignment> unitAssignments = assignmentMap.get("unit");
//
//        Set<AbstractAssignment> crowdAssignments = assignmentMap.get("crowd");

    }

}
