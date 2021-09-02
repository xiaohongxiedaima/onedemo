package com.xiaohongxiedaima.demo.javacore.objectheader;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * @author liusheng
 * @date 2021年05月25日 11:23 上午
 */
@Slf4j
public class ObjectInfoDemo {

    private short i = -2;

    public static void main(String[] args) {
        ObjectInfoDemo obj = new ObjectInfoDemo();

        log.info(ClassLayout.parseInstance(obj).toPrintable());
    }

}
