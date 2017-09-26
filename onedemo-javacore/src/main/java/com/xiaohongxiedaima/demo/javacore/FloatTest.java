package com.xiaohongxiedaima.demo.javacore;

/**
 * @author xiaohongxiedaima
 * @version 16-8-15
 * @E-mail redfishinaction@yahoo.com
 */
public class FloatTest {

    public static void main(String[] args) {
//        Float f = new Float(18.3f);
        float f = 18.3f;
        String s0 = String.valueOf(f * 10);
        String s1 = String.valueOf(f * 100);
        String s2 = String.valueOf(f * 1000);
        String s3 = String.valueOf(f * 10000);
        System.out.println(s0);
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);

    }
}
