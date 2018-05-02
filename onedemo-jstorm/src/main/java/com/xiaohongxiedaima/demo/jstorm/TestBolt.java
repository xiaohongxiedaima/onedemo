package com.xiaohongxiedaima.demo.jstorm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by liusheng on 18-4-2.
 */
public class TestBolt implements IRichBolt {

    private static final Logger LOG = LoggerFactory.getLogger(TestBolt.class);

    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {

    }

    public void execute(Tuple input) {
        int iVal = input.getIntegerByField("iVal");
        LOG.info("iVal: {}", iVal);
    }

    public void cleanup() {

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("iVal"));
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
