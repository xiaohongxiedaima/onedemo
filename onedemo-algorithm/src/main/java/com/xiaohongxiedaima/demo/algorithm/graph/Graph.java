package com.xiaohongxiedaima.demo.algorithm.graph;

import com.xiaohongxiedaima.demo.algorithm.graph.undirected.DFS;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Graph {

	private int v;
	private int e;

	private List<Integer>[] adj;

	/**
	* 6 
	* 7
	* 0 1
	* 0 2
	* 0 5
	* 1 2
	* 2 3
	* 2 4
	* 3 5
	* 3 4
	*/
	public Graph(InputStream is) {

		Scanner scanner = new Scanner(is);
		
		this.v = Integer.parseInt(scanner.nextLine());
		adj = new List[v];
		for (int i = 0; i < adj.length ; i ++) {
		 	adj[i] = new ArrayList<Integer>();
		 } 

		this.e = Integer.parseInt(scanner.nextLine());

		while(scanner.hasNextLine()) {
			String[] vs = scanner.nextLine().trim().split(" ");
			addEdge(Integer.parseInt(vs[0]), Integer.parseInt(vs[1]));
		}

	}

	private void addEdge(Integer v, Integer w) {
		adj[v].add(w);
		adj[w].add(v);
	}

	public Iterable<Integer> adj(Integer v) {
		return adj[v];
	}

	public String toString() {
		StringBuffer sBuffer = new StringBuffer();

		sBuffer.append("v: ").append(v).append(", ").append("e: ").append(e).append("\n");

		for(int i = 0 ; i < adj.length; i ++) {

		    sBuffer.append(i).append(" [ ");
			for (int j = 0; j < adj[i].size(); j ++) {
				sBuffer.append(adj[i].get(j)).append(" ");
			}
			sBuffer.append("]").append("\n");
		}

		return sBuffer.toString();
	}

    public Integer getV() {
        return this.v;
    }

	public static void main(String[] args) throws FileNotFoundException {
        String path = "/home/xiaohong/code/onedemo/onedemo-algorithm/src/main/resources/Graph.txt";
        Graph graph = new Graph(new FileInputStream(path));
        System.out.println(graph);

        DFS dfs = new DFS(graph, 2);

        System.out.println(dfs.count());

        System.out.println(dfs.marked(0));
        System.out.println(dfs.marked(7));

        dfs = new DFS(graph, 6);

        System.out.println(dfs.count());

        System.out.println(dfs.marked(0));
        System.out.println(dfs.marked(7));
	}

}