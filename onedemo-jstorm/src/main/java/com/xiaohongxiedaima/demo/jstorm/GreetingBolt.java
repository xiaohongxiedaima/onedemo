package com.xiaohongxiedaima.demo.jstorm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by liusheng on 17-9-12.
 */
public class GreetingBolt implements IRichBolt{
    private static Map<String, Integer> count = new TreeMap<String, Integer>();
    private final String morning = "morning";
    private final String noon = "noon";
    private final String afternoon = "afternoon";
    private final String evening = "evening";
    private static Integer total = 0;
    @Override
    public void cleanup() {
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {

    }

    @Override
    public void execute(Tuple input) {
        long timestamp= input.getLong(0);
//        System.out.println("bolt:"+timestamp);
        total+=1;
//        获取十位数
        long second = (timestamp/10)%10;
        if(second<3){
            System.out.println("bolt:"+morning);
            count.put(morning, (count.get(morning)==null)?1:count.get(morning)+1);
        }
        else if(second==3){
            System.out.println("bolt:"+noon);
            count.put(noon, (count.get(noon)==null)?1:count.get(noon)+1);
        }
        else if(second<8){
            System.out.println("bolt:"+afternoon);
            count.put(afternoon, (count.get(afternoon)==null)?1:count.get(afternoon)+1);
        }
        else{
            System.out.println("bolt:"+evening);
            count.put(evening, (count.get(evening)==null?1:count.get(evening)+1));
        }
        if(total%10==0){
            System.out.println("distribution show as followed:");
            System.out.println(morning+":"+1.0*((count.get(morning)==null)?0:count.get(morning))/total);
            System.out.println(noon+":"+1.0*((count.get(noon)==null?0:count.get(noon)))/total);
            System.out.println(afternoon+":"+1.0*((count.get(afternoon)==null?0:count.get(afternoon)))/total);
            System.out.println(evening+":"+1.0*((count.get(evening)==null?0:count.get(evening)))/total);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
