package com.xiaohongxiedaima.demo.javacore.dnf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by liusheng on 17-9-21.
 * 广告检索二层实现
 */
public class DNF {

    private static final Logger LOG = LoggerFactory.getLogger(DNF.class);

    private Map<Conjunction, Set<String>> first = new HashMap<Conjunction, Set<String>>();

    private Map<Integer, Map<Assignment, Set<Conjunction>>> second = new TreeMap<Integer, Map<Assignment, Set<Conjunction>>>();

    private void add(String id, List<List<String>> labels) {

        for (List<String> assignmentList : labels) {
            // 条件转换为 Java Bean
            List<Assignment> assignments = new ArrayList<Assignment>();
            for (String value : assignmentList) {
                Assignment assignment = new Assignment(value);
                assignments.add(assignment);
            }

            Conjunction conjunction = new Conjunction(assignments);
            // 一层
            if (first.containsKey(conjunction)) {
                Set<String> ads = first.get(conjunction);
                ads.add(id);
            } else {
                Set<String> ads = new HashSet<String>();
                ads.add(id);
                first.put(conjunction, ads);
            }

            // 二层
            Integer size = conjunction.getAssignmentList().size();
            Map<Assignment, Set<Conjunction>> assignmentConjunctionMapping = second.get(size);
            if (assignmentConjunctionMapping == null) {
                assignmentConjunctionMapping = new HashMap<Assignment, Set<Conjunction>>();
                second.put(size, assignmentConjunctionMapping);
            }
            for (Assignment assignment : conjunction.getAssignmentList()) {
                if (assignmentConjunctionMapping.containsKey(assignment)) {
                    Set<Conjunction> conjunctions = assignmentConjunctionMapping.get(assignment);
                    conjunctions.add(conjunction);
                } else {
                    Set<Conjunction> conjunctions = new HashSet<Conjunction>();
                    conjunctions.add(conjunction);
                    assignmentConjunctionMapping.put(assignment, conjunctions);
                }
            }
        }
    }

    public Set<String> query(List<String> userLabels) {
        Set<Conjunction> matchConjunctions = new HashSet<Conjunction>();
        // 第二层
        for (Map.Entry<Integer, Map<Assignment, Set<Conjunction>>> levelEntry : this.second.entrySet()) {
            Integer currentLevel = levelEntry.getKey();
            if (currentLevel <= userLabels.size() + 1) {
                Map<Assignment, Set<Conjunction>> assignmentConjunctionMapping = levelEntry.getValue();
                Map<Conjunction, Integer> conjunctionCountMap = new HashMap<Conjunction, Integer>();
                for (String userLabel : userLabels) {
                    Assignment assignment = new Assignment(userLabel);
                    Set<Conjunction> conjunctions = assignmentConjunctionMapping.get(assignment);
                    if (conjunctions != null) {
                        for (Conjunction conjunction : conjunctions) {
                            Integer count = conjunctionCountMap.get(conjunction);
                            if (count == null) {
                                count = 1;
                            } else {
                                count = count + 1;
                            }
                            conjunctionCountMap.put(conjunction, count);
                        }
                    }
                }
                for (Map.Entry<Conjunction, Integer> entry : conjunctionCountMap.entrySet()) {
                    if (entry.getValue() == currentLevel) {
                        matchConjunctions.add(entry.getKey());
                    }
                }
            }
        }

        // 第一层
        Set<String> ids = new HashSet<String>();
        for (Conjunction matchConjunction : matchConjunctions) {
            ids.addAll(first.get(matchConjunction));
        }

        return ids;
    }

    // TODO 通过 remove 修改不能展示的广告

    public static void main(String[] args) {
        DNF index = new DNF();

        List<List<String>> labels = Arrays.asList(
                Arrays.asList("a11", "a12", "a13"),
                Arrays.asList("a21", "a22", "a23"),
                Arrays.asList("a11", "a22")
        );
        for (int i = 10; i < 20;i ++) {
            index.add(String.valueOf(i), labels);
        }
        labels = Arrays.asList(
                Arrays.asList("a11", "a12", "a13"),
                Arrays.asList("a21", "a22", "a23"),
                Arrays.asList("a11")
        );
        for (int i = 1; i < 10;i ++) {
            index.add(String.valueOf(i), labels);
        }

        LOG.info(JSON.toJSONString(index.first, SerializerFeature.PrettyFormat));
        LOG.info(JSON.toJSONString(index.second, SerializerFeature.PrettyFormat));

        Set<String> ids = index.query(Arrays.asList("a11"));
        LOG.info(JSON.toJSONString(ids, SerializerFeature.PrettyFormat));
    }
}
