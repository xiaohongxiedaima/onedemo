package com.xiaohongxiedaima.demo.javacore.thread.volatiledemo;

/**
 * @author liusheng
 * @date 2021年03月30日 4:07 下午
 */
public class Clazz1 {
    private volatile Object a = new Object();

    public void resetA() {
        a = new Object();
    }

    public Object getA() {
        return a;
    }
}
