package com.xiaohongxiedaima.demo.javacore.classloader;

import com.sun.crypto.provider.DESKeyFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("{}", String.class.getClassLoader());

        log.info("{}", DESKeyFactory.class.getClassLoader());

        log.info("{}", CustomClassLoader.class.getClassLoader());
    }
}
