package com.xiaohongxiedaima.demo.algorithm.dnf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by liusheng on 17-9-28.
 */
public class InvertedIndex {

    private static final Logger LOG = LoggerFactory.getLogger(InvertedIndex.class);

    private Map<Conjunction, Set<String>> conjunctions = new ConcurrentHashMap<Conjunction, Set<String>>();

    private Map<Integer, Map<AbstractTerm, Pair<Operator, Set<Conjunction>>>> layeredTermMap =
            new ConcurrentSkipListMap<Integer, Map<AbstractTerm, Pair<Operator, Set<Conjunction>>>>();

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
            Integer layer = conjunction.getAssignments().size();
            Map<AbstractTerm, Pair<Operator, Set<Conjunction>>> termMap = layeredTermMap.get(layer);

            if (termMap == null) {
                termMap = new ConcurrentHashMap<AbstractTerm, Pair<Operator, Set<Conjunction>>>();
                layeredTermMap.put(layer, termMap);
            }

            for (AbstractAssignment assignment : conjunction.getAssignments()) {
                Set<AbstractTerm> termSet = assignment.createTerm();
                for (AbstractTerm term : termSet) {
                    Pair<Operator, Set<Conjunction>> pair = termMap.get(term);

                    if (pair == null) {
                        pair = new Pair<Operator, Set<Conjunction>>(assignment.operator, new HashSet<Conjunction>());
                        termMap.put(term, pair);
                    }

                    pair.getValue().add(conjunction);
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
            Integer layer = removedConjunction.getAssignments().size();
            Map<AbstractTerm, Pair<Operator, Set<Conjunction>>> termMap = layeredTermMap.get(layer);
            for (Map.Entry<AbstractTerm, Pair<Operator, Set<Conjunction>>> termMapEntry : termMap.entrySet()) {
                AbstractTerm term = termMapEntry.getKey();
                Pair<Operator, Set<Conjunction>> operatorConjunctionPair = termMapEntry.getValue();
                operatorConjunctionPair.getValue().remove(removedConjunction);
                if (operatorConjunctionPair.getValue().size() == 0) {
                    termMap.remove(term);
                }
            }
            if (termMap.size() == 0) {
                layeredTermMap.remove(layer);
            }
        }


    }

    public Set<String> query(Map<String, Object> terms) {
        Integer maxLayer = terms.size();

        Set<String> ids = new HashSet<String>();

        for (Map.Entry<Integer, Map<AbstractTerm, Pair<Operator, Set<Conjunction>>>> entry : layeredTermMap.entrySet()) {
            Integer currentLayer = entry.getKey();

            if (currentLayer > maxLayer) break;

            Map<Conjunction, Integer> matchCountMap = new HashMap<Conjunction, Integer>();

            for (Map.Entry<AbstractTerm, Pair<Operator, Set<Conjunction>>> termAndConjunction : entry.getValue().entrySet()) {
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
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
    }

}
