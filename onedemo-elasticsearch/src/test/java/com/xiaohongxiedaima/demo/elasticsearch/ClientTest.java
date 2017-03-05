package com.xiaohongxiedaima.demo.elasticsearch;

import com.xiaohongxiedaima.demo.elasticsearch.utils.ESClient;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by liusheng on 2017-03-05.
 */
public class ClientTest {

    @Test
    public void test() {
        TransportClient esClient = ESClient.getClient();
        Assert.assertNotNull(esClient);
        esClient.close();
    }

}
