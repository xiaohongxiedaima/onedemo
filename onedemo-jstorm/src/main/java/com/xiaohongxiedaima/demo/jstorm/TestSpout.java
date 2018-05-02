package com.xiaohongxiedaima.demo.jstorm;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import com.alibaba.jstorm.utils.JStormUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;

/**
 * Created by liusheng on 18-4-2.
 */
public class TestSpout implements IRichSpout {

    private static final Logger LOG = LoggerFactory.getLogger(TestSpout.class);

    private TopologyContext context;
    private SpoutOutputCollector collector;

    public TestSpout() {
        LOG.info("init");
    }

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        LOG.info("after task start");

        this.context = context;
        this.collector = collector;
    }

    public void close() {
        LOG.info("after task end");
    }

    public void activate() {

    }

    public void deactivate() {

    }

    public void nextTuple() {

        while (true) {
            JStormUtils.sleepMs(1000);
            Random random = new Random();
            int i = random.nextInt(1000);
            collector.emit(new Values(i));
        }

    }

    public void ack(Object msgId) {
        LOG.info("ack msgId: {}", msgId);
    }

    public void fail(Object msgId) {
        LOG.info("fail msgId: {}", msgId);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("iVal"));
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
