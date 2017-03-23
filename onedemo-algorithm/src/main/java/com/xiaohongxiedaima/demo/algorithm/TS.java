package com.xiaohongxiedaima.demo.algorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liusheng on 2017-03-16.
 * retriever tree
 */
public class TS {

    private TSNode root = null;

    public void insert(String s) {
        if (s == null || s.length() == 0) {
            return;
        }

        if (root == null) {
            root = new TSNode(s.charAt(0), new HashMap<Character, TSNode>());
        }
        TSNode currentNode = root;
        int idx = 0;
        while (true) {
            if (currentNode.getSplitChar().equals(s.charAt(idx))) {

            }

        }

    }

}

class TSNode {
    private Character splitChar;
    private Map<Character, TSNode> children;

    public TSNode(Character splitChar, Map<Character, TSNode> children) {
        this.splitChar = splitChar;
        this.children = children;
    }

    public Character getSplitChar() {
        return splitChar;
    }

    public void setSplitChar(Character splitChar) {
        this.splitChar = splitChar;
    }

    public Map<Character, TSNode> getChildren() {
        return children;
    }

    public void setChildren(Map<Character, TSNode> children) {
        this.children = children;
    }
}
