package com.xiaohongxiedaima.demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.ql.metadata.Hive;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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

        return job.waitForCompletion(true);

    }

    final class HiveMapper extends Mapper<LongWritable, TextInputFormat, TextOutputFormat, TextOutputFormat> {
        @Override
        protected void map(LongWritable key, TextInputFormat value, Context context) throws IOException, InterruptedException {
            super.map(key, value, context);
        }
    }

}
