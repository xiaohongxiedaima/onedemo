package com.xiaohongxiedaima.demo.jstorm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.TopologyBuilder;
import com.alibaba.jstorm.utils.JStormUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shade.storm.org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by liusheng on 18-3-30.
 */
public class TestTopology {

    private static final Logger LOG = LoggerFactory.getLogger(TestTopology.class);

    private static Map conf = new HashMap<Object, Object>();

    public static void loadProperty(String prop) {
        Properties properties = new Properties();

        try {
            InputStream stream = new FileInputStream(prop);
            properties.load(stream);
        } catch (FileNotFoundException e) {
            System.out.println("No such file " + prop);
        } catch (Exception e1) {
            e1.printStackTrace();

            return;
        }

        conf.putAll(properties);
    }

    public static void loadYaml(String confPath) {

        Yaml yaml = new Yaml();

        try {
            InputStream stream = new FileInputStream(confPath);

            conf = (Map) yaml.load(stream);
            if (conf == null || conf.isEmpty() == true) {
                throw new RuntimeException("Failed to read config file");
            }

        } catch (FileNotFoundException e) {
            System.out.println("No such file " + confPath);
            throw new RuntimeException("No config file");
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new RuntimeException("Failed to read config file");
        }

        return;
    }

    public static void loadConf(String arg) {
        if (arg.endsWith("yaml")) {
            loadYaml(arg);
        } else {
            loadProperty(arg);
        }
    }

    private final static String TOPOLOGY_SPOUT_PARALLELISM_HINT = "spout.parallel";
    private final static String TOPOLOGY_BOLT_PARALLELISM_HINT  = "bolt.parallel";

    private static void distributed(String[] args) {

    }

    private static void local(String[] args) throws AlreadyAliveException, InvalidTopologyException, InterruptedException {
        if (args.length == 0) {
            System.err.println("Please input configuration file");
            System.exit(-1);
        }

        loadConf(args[0]);

        String streamName = (String) conf.get(Config.TOPOLOGY_NAME);
        if (streamName == null) {
            String[] className = Thread.currentThread().getStackTrace()[1].getClassName().split("\\.");
            streamName = className[className.length - 1];
        }

        TopologyBuilder builder = new TopologyBuilder();

        int spoutParallelismHint = JStormUtils.parseInt(conf.get(TOPOLOGY_SPOUT_PARALLELISM_HINT), 1);
        int boltParallelismHint = JStormUtils.parseInt(conf.get(TOPOLOGY_BOLT_PARALLELISM_HINT), 2);

        builder.setSpout("spout", new TestSpout(), spoutParallelismHint);

        BoltDeclarer boltDeclarer = builder.setBolt("bolt", new TestBolt(), boltParallelismHint);
        boltDeclarer.localOrShuffleGrouping("spout");

        conf.put(Config.STORM_CLUSTER_MODE, "local");

        LocalCluster localCluster = new LocalCluster();

        localCluster.submitTopology(streamName, conf, builder.createTopology());

        Thread.sleep(1000 * 60 * 10l);

        localCluster.killTopology(streamName);
        localCluster.shutdown();
//        StormSubmitter.submitTopology(streamName, conf, builder.createTopology());
    }


    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException, InterruptedException {
        local(args);
    }

}
