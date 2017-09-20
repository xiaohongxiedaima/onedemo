package com.xiaohongxiedaima.demo.jstorm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liusheng on 17-9-12.
 */
public class TestTopology {
    private static final Logger LOG = LoggerFactory.getLogger(TestTopology.class);

    public static void main(String[] args) throws InterruptedException {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("TimeStampSpout", new TimeStampSpout());
        builder.setBolt("GreetingBolt",new GreetingBolt()).localOrShuffleGrouping("TimeStampSpout");

        LocalCluster cluster = new LocalCluster();

        Config config = new Config();

        cluster.submitTopology("test", config, builder.createTopology());

//        Thread.sleep(10000l);
    }
}
