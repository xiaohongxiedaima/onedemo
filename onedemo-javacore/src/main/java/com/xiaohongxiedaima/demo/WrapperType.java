package com.xiaohongxiedaima.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hadoop on 16-6-22.
 */
public class WrapperType {

    private static final Logger logger = LoggerFactory.getLogger(WrapperType.class);

    public static void main(String[] args) {
        Class<?> intClass = int.class;
        Class<?> integerClass = Integer.class;

        logger.info("intClass: {}", intClass);
        logger.info("integerClass: {}", integerClass);
        logger.info("intClass == integerClass : {}", intClass == integerClass);
    }

}
