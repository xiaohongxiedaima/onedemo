package com.xiaohongxiedaima.demo.algorithm;

import org.junit.Test;

/**
 * Created by liusheng on 2017-03-16.
 */
public class BSTTest {

    @Test
    public void testInsert() {
        BST<String, String> bst = new BST<String, String>();
        bst.insert("hello", "world");
        bst.insert("world", "hello");
        bst.insert("hello world", "hello world");
    }

    @Test
    public void testOrder() {
        Integer[] keys = new Integer[] {
                4, 3, 8, 1, 7, 16, 2, 10, 9, 14
        };

        Integer[] values = new Integer[] {
                4, 3, 8, 1, 7, 16, 2, 10, 9, 14
        };

        BST<Integer, Integer> bst = new BST<Integer, Integer>();

        bst.create(keys, values);

        bst.printOrder(bst.getRoot());
        bst.printPreOrder(bst.getRoot());
        bst.printPostOrder(bst.getRoot());
    }


}
