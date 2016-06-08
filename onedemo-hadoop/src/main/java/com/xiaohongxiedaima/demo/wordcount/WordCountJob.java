package com.xiaohongxiedaima.demo.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaohongxiedaima
 * @version 16/6/8
 * @E-mail redfishinaction@yahoo.com
 */
public class WordCountJob {

    private static final String JOB_NAME = "WordCountJob";

    public static class WordCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] words = value.toString().replaceAll(",", "").split(" ");
            Map<String, Long> countMap = new HashMap<String, Long>();
            for (String word : words) {
                Long count = countMap.get(word);
                if (count == null) {
                    count = 1l;
                } else {
                    count = count + 1;
                }
                countMap.put(word, count);
            }

            for (Map.Entry<String, Long> entry : countMap.entrySet()) {
                context.write(new Text(entry.getKey()), new LongWritable(entry.getValue()));
            }

        }
    }

    public static class WordCountReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {

            Long total = 0l;
            for (LongWritable value : values) {
                total += value.get();
            }

            context.write(key, new LongWritable(total));

        }
    }

    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();

        JobConf jobConf = new JobConf(configuration);

        Job job = Job.getInstance(jobConf);

        job.setJarByClass(WordCountJob.class);
        job.setJobName(JOB_NAME);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }

}
