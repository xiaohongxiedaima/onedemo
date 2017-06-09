package com.xiaohongxiedaima.demo.algorithm.graph.undirected;

import com.xiaohongxiedaima.demo.algorithm.graph.Graph;
import com.xiaohongxiedaima.demo.algorithm.graph.Search;

/**
 * Created by xiaohong on 17-5-18.
 */
public class DFS extends Search{

    private Boolean[] marked;
    private Integer count;

    public DFS(Graph graph, Integer v) {
        super(graph, v);

        marked = new Boolean[graph.getV()];
        for (int i = 0 ; i < marked.length; i ++) {
            marked[i] = false;
        }
        count = 0;
        dfs(graph, v);
    }

    private void dfs(Graph graph, Integer v) {
        marked[v] = true;
        count ++;

        Iterable<Integer> adj = graph.adj(v);
        for (Integer s : adj) {
            if (marked[s] == false) {
                dfs(graph, s);
            }
        }
    }

    public boolean marked(Integer w) {
        return marked[w];
    }

    public int count() {
        return this.count;
    }

}
