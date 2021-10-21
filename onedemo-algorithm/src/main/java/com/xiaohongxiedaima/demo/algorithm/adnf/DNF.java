package com.xiaohongxiedaima.demo.algorithm.adnf;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.xiaohongxiedaima.demo.algorithm.adnf.Conjunction.Assignment.Term;

/**
 * @author liusheng
 * @date 2021年08月30日 5:44 下午
 *
 */
@Slf4j
public class DNF {

    private Map<Integer, Map<Term, Set<Integer>>> index = new HashMap<>();

    /**
     * 将广告对应的定向条件添加到索引中
     * @param id 广告id
     * @param json 定向条件 {"age": ["30"], "geo": ["北京"], "gender": ["男", "女"]}
     */
    public void add(Integer id, String json) {
        Conjunction conjunction = Conjunction.parse(json);

        // 第 N 层 Term 到广告id的映射
        // <Term, [广告ID]>
        Map<Term, Set<Integer>> termAdIdMap = index.computeIfAbsent(conjunction.getSize(), k -> new HashMap<>());

        List<Term> termList = conjunction.toTermList();
        for (Term term : termList) {
            Set<Integer> set = termAdIdMap.computeIfAbsent(term, k -> new TreeSet<>());
            set.add(id);
        }
    }

    /**
     * 查询索引
     * @param queryTermList 查询条件
     * @return
     */
    public List<Integer> query(List<Term> queryTermList) {
        List<Integer> list = new ArrayList<>();

        for (int level = 1; level <= queryTermList.size(); level++) {
            list.addAll(queryByLevel(level, queryTermList));
        }

        return list;
    }

    /**
     * 分层查询匹配的广告
     * @param level
     * @param queryTermList 查询条件
     * @return
     */
    private List<Integer> queryByLevel(int level, List<Term> queryTermList) {
        Map<Term, Set<Integer>> termAdIdMap = index.get(level);
        // 没有sizeOf(Conjunction) == level的情况, 即对应 level 没有广告
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

    /**
     * 广告id 在 termMatchList 中出现 level 次，那么这个广告id 符合查询条件
     * @param level 执行的是第几层的匹配
     * @param termMatchList [[广告id1, 广告id2],[广告id1, 广告id3]]
     * @return
     */
    private List<Integer> countAndFilter1(int level, List<List<Integer>> termMatchList) {
        Map<Integer, Integer> countMap = new HashMap<>();

        termMatchList.forEach(list -> list.forEach(adid -> {
            Integer count = countMap.getOrDefault(adid, 0);
            countMap.put(adid, ++count);
        }));

        List<Integer> matchAdIdList = countMap.entrySet().stream()
                .filter(entry -> entry.getValue() == level)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return matchAdIdList;
    }

    /**
     * 打印底层索引数据
     */
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
                log.info(line);
            }
            log.info("");
        }
    }

}
