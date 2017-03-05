package com.xiaohongxiedaima.demo.elasticsearch.utils;

import com.xiaohongxiedaima.demo.utils.PropUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Created by liusheng on 2017-03-05.
 */
public class ESClient {

    private static final Logger LOG = LoggerFactory.getLogger(ESClient.class);

    private static TransportClient esClient = null;

    private static final void init() {

        String confPath = "es.properties";
        Properties props = PropUtils.load(ESClient.class, confPath);
        String clusterName = props.getProperty("cluster.name");
        String hostPorts = props.getProperty("host-port");

        Settings settings = Settings.builder()
                .put("cluster.name", clusterName).build();

        esClient = new PreBuiltTransportClient(settings);
        for (String hostPort : hostPorts.split(",")) {
            String host = hostPort.split(":")[0];
            String port = hostPort.split(":")[1];
            try {
                esClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), Integer.parseInt(port)));
            } catch (UnknownHostException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    public static final TransportClient getClient() {
        if (esClient == null) {
            init();
        }
        return esClient;
    }

    public static void close() {
        esClient.close();
    }



}
