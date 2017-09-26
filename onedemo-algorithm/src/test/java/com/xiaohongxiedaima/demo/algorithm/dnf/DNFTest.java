package com.xiaohongxiedaima.demo.algorithm.dnf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by liusheng on 17-9-26.
 */
public class DNFTest {

    private static final Logger LOG = LoggerFactory.getLogger(DNFTest.class);

    @Test
    public void testAdd() {
        DNF index = new DNF();

        List<List<String>> labels = Arrays.asList(
                Arrays.asList("a11", "a12", "a13"),
                Arrays.asList("a21", "a22", "a23"),
                Arrays.asList("a11", "a22")
        );
        for (int i = 10; i < 20;i ++) {
            index.add(String.valueOf(i), labels);
        }
        labels = Arrays.asList(
                Arrays.asList("a11", "a12", "a13"),
                Arrays.asList("a21", "a22", "a23"),
                Arrays.asList("a11")
        );
        for (int i = 1; i < 10;i ++) {
            index.add(String.valueOf(i), labels);
        }

//        LOG.info(JSON.toJSONString(index.first, SerializerFeature.PrettyFormat));
//        LOG.info(JSON.toJSONString(index.second, SerializerFeature.PrettyFormat));
//
//        Set<String> ids = index.query(Arrays.asList("a11"));
//        LOG.info(JSON.toJSONString(ids, SerializerFeature.PrettyFormat));
    }

    public void testQuery() {

    }

}
