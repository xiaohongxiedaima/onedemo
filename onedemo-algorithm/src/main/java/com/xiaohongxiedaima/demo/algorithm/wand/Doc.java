package com.xiaohongxiedaima.demo.algorithm.wand;

import java.util.Map;

/**
 * Created by liusheng on 17-9-26.
 */
public class Doc implements Comparable<Doc>{

    private String id;
    private Map<String, Integer> termMap;

    public Doc() {
    }

    public Doc(Map<String, Integer> termMap) {
        this.termMap = termMap;
    }

    public Map<String, Integer> getTermMap() {
        return termMap;
    }

    public void setTermMap(Map<String, Integer> termMap) {
        this.termMap = termMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int compareTo(Doc doc) {
        return this.id.compareTo(doc.getId());
    }
}
