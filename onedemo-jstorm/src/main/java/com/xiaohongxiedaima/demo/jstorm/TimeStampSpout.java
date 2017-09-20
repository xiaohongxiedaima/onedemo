package com.xiaohongxiedaima.demo.jstorm;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * Created by liusheng on 17-9-12.
 */
public class TimeStampSpout implements IRichSpout {
    private SpoutOutputCollector collector;

    @Override
    public void nextTuple() {
        long now = System.currentTimeMillis();
        Values tuple = new Values(now);
        System.out.println("spout:"+tuple);
        this.collector.emit(tuple);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void ack(Object msgId) {

    }

    public void fail(Object msgId) {

    }

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    public void close() {

    }

    public void activate() {

    }

    public void deactivate() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("timestamp"));
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

}