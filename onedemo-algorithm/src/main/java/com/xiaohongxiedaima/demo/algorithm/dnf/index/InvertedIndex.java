package com.xiaohongxiedaima.demo.algorithm.dnf.index;

import com.xiaohongxiedaima.demo.algorithm.dnf.index.assignment.AbstractAssignment;
import com.xiaohongxiedaima.demo.algorithm.dnf.index.term.AbstractTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by liusheng on 17-10-13.
 */
public class InvertedIndex {
    private static final Logger LOG = LoggerFactory.getLogger(InvertedIndex.class);

    private Map<Conjunction, Set<String>> conjunctions = new ConcurrentHashMap<Conjunction, Set<String>>();

    private Map<Integer, Map<AbstractTerm, Set<Conjunction>>> layeredTermMap =
            new ConcurrentSkipListMap<Integer, Map<AbstractTerm, Set<Conjunction>>>();

    public void add(List<Conjunction> conjunctionList, String id) {

        for (Conjunction conjunction : conjunctionList) {

            // 一层索引
            Set<String> ids = conjunctions.get(conjunction);
            if (ids == null) {
                ids = new HashSet<String>();
                conjunctions.put(conjunction, ids);
            }
            ids.add(id);

            // 二层索引
            /** 不会存在 layer == 0 的情况*/
            Integer layer = conjunction.getEqCount();

            Map<AbstractTerm, Set<Conjunction>> termMap = layeredTermMap.get(layer);

            if (termMap == null) {
                termMap = new ConcurrentHashMap<AbstractTerm, Set<Conjunction>>();
                layeredTermMap.put(layer, termMap);
            }

            for (AbstractAssignment assignment : conjunction.getAssignments()) {
                Set<AbstractTerm> termSet = assignment.createTerm();
                for (AbstractTerm term : termSet) {
                    if (assignment.getOperator().equals(Operator.EQ)) {
                        Set<Conjunction> cSet = termMap.get(term);
                        if (cSet == null) {
                            cSet = new HashSet<Conjunction>();
                            termMap.put(term, cSet);
                        }
                        cSet.add(conjunction);
                    } else {
                        Map<AbstractTerm, Set<Conjunction>> neTermMap = layeredTermMap.get(0);
                        if (neTermMap == null) {
                            neTermMap = new ConcurrentHashMap<AbstractTerm, Set<Conjunction>>();
                            layeredTermMap.put(0, neTermMap);
                        }
                        Set<Conjunction> cSet = neTermMap.get(term);
                        if (cSet == null) {
                            cSet = new HashSet<Conjunction>();
                            neTermMap.put(term, cSet);
                        }
                        cSet.add(conjunction);
                    }
                }
            }
        }
    }


    public void remove(Set<String> ids) {
        // 移除第一层
        Set<Conjunction> removedConjunctions = new HashSet<Conjunction>();

        for (Map.Entry<Conjunction, Set<String>> entry : conjunctions.entrySet()) {
            for (String id : ids) {
                entry.getValue().remove(id);
                if (entry.getValue().size() == 0) {
                    removedConjunctions.add(entry.getKey());
                    conjunctions.remove(entry.getKey());
                }
            }
        }
        // 第二层
        for (Conjunction removedConjunction : removedConjunctions) {
            removeConjunctionInSpecialLayer(0, removedConjunction);
            Integer layer = removedConjunction.getEqCount();
            removeConjunctionInSpecialLayer(layer, removedConjunction);
        }
    }

    private void removeConjunctionInSpecialLayer(Integer layer, Conjunction removedConjunction) {
        Map<AbstractTerm, Set<Conjunction>> termMap = layeredTermMap.get(layer);
        for (Map.Entry<AbstractTerm,Set<Conjunction>> termMapEntry : termMap.entrySet()) {
            AbstractTerm term = termMapEntry.getKey();
            Set<Conjunction> cSet = termMapEntry.getValue();
            cSet.remove(removedConjunction);
            if (cSet.size() == 0) {
                termMap.remove(term);
            }
        }
        if (termMap.size() == 0) {
            layeredTermMap.remove(layer);
        }
    }

    public Set<String> query(Set<AbstractTerm> termSet) {

        Map<AbstractTerm, Set<Conjunction>> neTermMap = layeredTermMap.get(0);
        Set<String> ids = new HashSet<String>();

        for (Map.Entry<Integer, Map<AbstractTerm, Set<Conjunction>>> entry : layeredTermMap.entrySet()) {
            if (entry.getKey() > termSet.size()) break;

            Map<AbstractTerm, Set<Conjunction>> eqTermMap = entry.getValue();

            Map<Conjunction, Integer> matchCountMap = new HashMap<Conjunction, Integer>();

            for (AbstractTerm term : termSet) {
                // eq
                if (eqTermMap.containsKey(term)) {
                    Set<Conjunction> matchSet = entry.getValue().get(term);
                    for (Conjunction matchC : matchSet) {
                        Integer count = matchCountMap.get(matchC);
                        if (count == null) {
                            count = 1;
                        } else {
                            count++;
                        }
                        matchCountMap.put(matchC, count);
                    }
                }
                // ne
                if (neTermMap != null && neTermMap.containsKey(term)) {
                    Set<Conjunction> matchSet = neTermMap.get(term);
                    for (Conjunction matchC : matchSet) {
                        Integer count = matchCountMap.get(matchC);
                        if (count == null) {
                            count = -1;
                        } else {
                            count--;
                        }
                        matchCountMap.put(matchC, count);
                    }
                }
            }
            for (Map.Entry<Conjunction, Integer> cCountEntry : matchCountMap.entrySet()) {
                if (cCountEntry.getValue() == entry.getKey()) {
                    ids.addAll(conjunctions.get(cCountEntry.getKey()));
                }
            }
        }

        return ids;

    }
}
