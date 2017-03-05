package com.xiaohongxiedaima.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Created by liusheng on 2017-03-05.
 */
public class PropUtils {

    private static final Logger LOG = LoggerFactory.getLogger(PropUtils.class);

    public static final Properties load(Class<?> clazz, String path) {
        InputStream inputStream = clazz.getClassLoader().getResourceAsStream(path);
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            LOG.info("load properties by: {}, from: {}", clazz, path);
            for (Map.Entry entry : properties.entrySet()) {
                LOG.info("key: {}, value: {}", entry.getKey(), entry.getValue());
            }
        }
        return properties;
    }

}
