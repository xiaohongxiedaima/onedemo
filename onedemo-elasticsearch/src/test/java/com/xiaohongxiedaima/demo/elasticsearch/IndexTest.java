package com.xiaohongxiedaima.demo.elasticsearch;

import com.xiaohongxiedaima.demo.elasticsearch.utils.ESClient;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liusheng on 2017-03-05.
 */
public class IndexTest {

    private static final Logger LOG = LoggerFactory.getLogger(IndexTest.class);

    private static final TransportClient esClient = ESClient.getClient();

    @Test
    public void test() {

        String[] title = new String[] {
                "二居室2房间", "居住地"
        };
        String[] title1 = new String[] {
                "hello word", "hi elasticsearch"
        };
        String[] title2 = new String[] {
                "how are you", "are you ls"
        };

        for (int i = 0 ; i < title.length; i ++) {
            Map<String, String> source = new HashMap<String, String>();
            source.put("title", title[i]);
            source.put("title1", title1[i]);
            source.put("title2", title2[i]);
            IndexResponse indexResponse = esClient.prepareIndex("demo", "demo")
                    .setSource(source).get();
            Assert.assertEquals(201, indexResponse.status().getStatus());
        }

    }


}
