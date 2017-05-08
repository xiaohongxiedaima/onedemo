package com.xiaohongxiedaima.demo.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liusheng on 2017-03-16.
 *
 * 二叉搜索树
 */
public class BST<K extends Comparable, V> {

    private static final Logger LOG = LoggerFactory.getLogger(BST.class);

    private BSTNode root;

    private void recursiveInsert(BSTNode currentNode, K key, V value) {

        int result = currentNode.getKey().compareTo(key);
        if (result > 0) { // 添加到左子树
            BSTNode left = currentNode.getLeft();
            if (left == null) {
                left = new BSTNode(key, value);
                left.setParent(currentNode);
                currentNode.setLeft(left);
            } else {
                recursiveInsert(left, key, value);
            }

        } else if (result < 0){ // 添加到右子树
            BSTNode rigth = currentNode.getRigth();
            if (rigth == null) {
                rigth = new BSTNode(key, value);
                rigth.setParent(currentNode);
                currentNode.setRigth(rigth);
            } else {
                recursiveInsert(rigth, key, value);
            }
        } else { // 覆盖当前 value 值
            currentNode.setValue(value);
        }

    }

    public void insert(K key, V value) {
        if (root == null) {
            root = new BSTNode(key, value);
            root.setParent(null);
            return;
        }
        recursiveInsert(root, key, value);
    }

    public void create(K[] keys, V[] values) {
        for (int i = 0; i < keys.length ; i ++) {
            insert(keys[i], values[i]);
        }
    }

    /**
     * 查找二叉搜索树中的指定节点
     * @param key
     * @return
     */
    public BSTNode lookup(K key) {
        BSTNode node = root;

        while (node != null && !node.getKey().equals(key)) {
            Integer r = node.getKey().compareTo(key);
            if (r > 0) {
                node = node.getLeft();
            } else if (r < 0) {
                node =  node.getRigth();
            } else {
                return node;
            }
        }

        return null;
    }

    /**
     * 先序遍历 先访问跟节点 再访问左子树 再访问右子树
     * @param root
     */
    public void printOrder(BSTNode root) {

        LOG.info("<{}, {}>", root.getKey(), root.getValue());

        if (root.getLeft() != null) {
            printOrder(root.getLeft());
        }

        if (root.getRigth() != null) {
            printOrder(root.getRigth());
        }

    }

    /**
     * 中序遍历 先访问左字数 再访问根节点 再访问右子树
     * @param root
     */
    public void printPreOrder(BSTNode root) {

        if (root.getLeft() != null) {
            printPreOrder(root.getLeft());
        }

        LOG.info("<{}, {}>", root.getKey(), root.getValue());

        if (root.getRigth() != null) {
            printPreOrder(root.getRigth());
        }

    }

    /**
     * 后续遍历
     * @param root
     */
    public void printPostOrder(BSTNode root) {

        if (root.getLeft() != null) {
            printPostOrder(root.getLeft());
        }

        if (root.getRigth() != null) {
            printPostOrder(root.getRigth());
        }

        LOG.info("<{}, {}>", root.getKey(), root.getValue());

    }


    public BSTNode getRoot() {
        return root;
    }
}



class BSTNode<K extends Comparable, V> {
    private K key;
    private V value;
    private BSTNode left, rigth, parent;

    public BSTNode(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public BSTNode getLeft() {
        return left;
    }

    public void setLeft(BSTNode left) {
        this.left = left;
    }

    public BSTNode getRigth() {
        return rigth;
    }

    public void setRigth(BSTNode rigth) {
        this.rigth = rigth;
    }

    public BSTNode getParent() {
        return parent;
    }

    public void setParent(BSTNode parent) {
        this.parent = parent;
    }
}
