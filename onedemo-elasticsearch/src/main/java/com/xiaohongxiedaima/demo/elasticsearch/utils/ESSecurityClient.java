package com.xiaohongxiedaima.demo.elasticsearch.utils;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by liusheng on 17-12-20.
 */
public class ESSecurityClient {

    public static void main(String[] args) throws UnknownHostException, ExecutionException, InterruptedException {

        TransportClient client = new PreBuiltXPackTransportClient(Settings.builder()
                .put("cluster.name", "elasticsearch")
                .put("xpack.security.user", "elastic:elastic")
                .build());


        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.1.100"), 9300));


        GetResponse getResponse = client.prepareGet("zipkin:span-2017-12-20", "span", "AWBx5x38GRQQBTMNPZrM").execute().get();
        for (Map.Entry<String, Object> entry : getResponse.getSource().entrySet()) {
            System.out.println(entry.getKey() + "," + entry.getValue());
        }

    }

}
