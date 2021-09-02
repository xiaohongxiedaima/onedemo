package com.xiaohongxiedaima.demo.algorithm.adnf;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.*;

import static com.xiaohongxiedaima.demo.algorithm.adnf.Conjunction.Assignment.Term;

/**
 * @author liusheng
 * @date 2021年08月30日 5:44 下午
 *
 */
@Slf4j
public class DNF {

    private Map<Integer, Map<Term, Set<Integer>>> index = new HashMap<>();

    public void add(Integer id, String json) {
        Conjunction conjunction = Conjunction.parse(json);

        Map<Term, Set<Integer>> termAdIdMap = index.computeIfAbsent(conjunction.getSize(), k -> new HashMap<>());

        List<Term> termList = conjunction.toTermList();
        for (Term term : termList) {
            Set<Integer> set = termAdIdMap.computeIfAbsent(term, k -> new TreeSet<>());

            set.add(id);
        }
    }

    public List<Integer> query(List<Term> queryTermList) {
        List<Integer> list = new ArrayList<>();

        for (int level = 1; level <= queryTermList.size(); level++) {
            list.addAll(queryByLevel(level, queryTermList));
        }

        return list;
    }

    private List<Integer> queryByLevel(int level, List<Term> queryTermList) {
        Map<Term, Set<Integer>> termAdIdMap = index.get(level);
        // 某一层没有广告
        if (termAdIdMap == null) {
            return new ArrayList<>();
        }

        List<List<Integer>> termMatchList = new ArrayList<>();

        for (Term queryTerm : queryTermList) {
            Set<Integer> adIdSet = termAdIdMap.get(queryTerm);

            // 没有广告配置这个定向条件
            if (adIdSet != null) {
                termMatchList.add(new ArrayList<>(adIdSet));
            }
        }

        if (termMatchList.size() == 0) {
            return new ArrayList<>();
        }
        return countAndFilter1(level, termMatchList);
    }

    private List<Integer> countAndFilter1(int level, List<List<Integer>> termMatchList) {
        Map<Integer, Integer> countMap = new HashMap<>();

        termMatchList.forEach(list -> list.forEach(adid -> {
            Integer count = countMap.getOrDefault(adid, 0);
            countMap.put(adid, ++count);
        }));

        List<Integer> matchAdIdList = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() == level) {
                matchAdIdList.add(entry.getKey());
            }
        }

        return matchAdIdList;
    }

    public void print() {
        Gson gson = new Gson();
        for (Map.Entry<Integer, Map<Term, Set<Integer>>> sizeEntry : index.entrySet()) {
            Integer size = sizeEntry.getKey();
            Map<Term, Set<Integer>> termAdIdMap = sizeEntry.getValue();
            boolean sizeFlag = true;
            for (Map.Entry<Term, Set<Integer>> termEntry : termAdIdMap.entrySet()) {
                Term term = termEntry.getKey();

                Set<Integer> adIdSet = termEntry.getValue();

                String format = "%s\t%s\t%s";
                String sizeStr = sizeFlag ? String.valueOf(size) : "";
                String termStr = term.getKey() + "=" + term.getValue();
                termStr = StringUtils.rightPad(termStr, 20);
                if (sizeFlag) {
                    sizeFlag = false;
                }

                String line = String.format(format, sizeStr, termStr, gson.toJson(adIdSet));
                System.out.println(line);
            }
            System.out.println("");
        }
    }

}
