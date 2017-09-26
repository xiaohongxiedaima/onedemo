package com.xiaohongxiedaima.demo.algorithm.wand;

import java.util.*;

/**
 * Created by liusheng on 17-9-26.
 */
public class Wand {

    private Map<String, SortedSet<Doc>> invertedIndex = new TreeMap();

    private SortedSet<String> topK = new TreeSet<String>();

    public Wand() {
    }

    public void add(String id, List<String> termList) {
        Doc doc = new Doc();
        Map<String, Integer> termMap = new HashMap<String, Integer>();

        for (String term : termList) {
            Integer count = termMap.get(term);
            if (count == null) {
                count = 1;
            } else {
                count ++;
            }
            termMap.put(term, count);
        }
        doc.setId(id);
        doc.setTermMap(termMap);

        for (String term : termList) {
            SortedSet<Doc> docSet = invertedIndex.get(term);
            if (docSet == null) {
                docSet = new TreeSet<Doc>();
                docSet.add(doc);
            }
        }
    }

    public void query(List<String> termList) {

        Map<String, SortedSet<Doc>> map = new TreeMap<String, SortedSet<Doc>>();

        for (String term : termList) {
            if (invertedIndex.containsKey(term)) {
                map.put(term, invertedIndex.get(term));
            }
        }

        Double minScore = 0.0d;
        String currentId;
        Double currentScore = 0.0d;

        for (Map.Entry<String, SortedSet<Doc>> entry : map.entrySet()) {

            String term = entry.getKey();
            SortedSet<Doc> docSet = entry.getValue();

            for (Doc doc : docSet) {

                Double adScore = Math.random();



            }


        }


    }

    public static void main(String[] args) {
        
        
        
    }

}
