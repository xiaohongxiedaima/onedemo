package com.xiaohongxiedaima.demo;

import javafx.scene.control.Tab;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.StorageDescriptor;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.ql.metadata.Hive;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaohongxiedaima
 * @version 16/6/29
 * @E-mail redfishinaction@yahoo.com
 */
public class HiveJob {

    private static final Logger logger = LoggerFactory.getLogger(HiveJob.class);

    private static final String JOB_NAME = "[HIVE2HBASE]_hivejob";

    public static boolean run(Configuration configuration, Table table) throws IOException, ClassNotFoundException, InterruptedException {

        JobConf jobConf = new JobConf(configuration);
        Job job = Job.getInstance(jobConf);
        job.setJobName(JOB_NAME + "_" + table.getDbName() + "." + table.getTableName());
        job.setJarByClass(HiveJob.class);

        String inputFormat = table.getSd().getInputFormat();
        job.setInputFormatClass((Class<? extends InputFormat>) Class.forName(inputFormat));


        return job.waitForCompletion(true);

    }

    final class HiveMapper extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            super.map(key, value, context);
        }
    }

    final class HiveReducer extends Reducer<Text, Text, Text, Text> {

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            super.reduce(key, values, context);
        }
    }


}
