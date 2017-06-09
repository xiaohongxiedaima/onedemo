package com.xiaohongxiedaima.demo.algorithm.graph;

/**
 * Created by xiaohong on 17-5-18.
 */
public abstract class Search {
    public Search(Graph graph, Integer v) {

    }

    public abstract boolean marked(Integer w);
    public abstract int count();
}
