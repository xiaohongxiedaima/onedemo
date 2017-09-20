package com.xiaohongxiedaima.demo.spark

import java.util.Date

import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2
import org.apache.hadoop.hbase.{HBaseConfiguration, KeyValue}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author xiaohongxiedaima
  * @version 16/6/8
  * @E-mail redfishinaction@yahoo.com
  */
object WriteHFile {

  def main(args: Array[String]) {
    val sparkConf = new SparkConf()
    sparkConf.setAppName("WriteHFile")

    val sc = new SparkContext(sparkConf)
    val hbaseConf = HBaseConfiguration.create()
    sc.parallelize(Array(
      ("rowkey1", "cf", "qualifier", "value1", new Date()),
      ("rowkey1", "cf", "qualifier", "value2", new Date()),
      ("rowkey2", "cf", "qualifier", "value3", new Date())
    )).groupBy(_._1)
      .map(x => {
        x._2.maxBy(_._5)
      })
      .map(x => {
        val rowkeyInBytes = x._1.getBytes
        val writableRowkey = new ImmutableBytesWritable(rowkeyInBytes)
        val keyValue = new KeyValue(rowkeyInBytes, x._2.getBytes, x._3.getBytes, x._4.getBytes)
        (writableRowkey, keyValue)
      })
      .saveAsNewAPIHadoopFile(
        "path",
        classOf[ImmutableBytesWritable],
        classOf[KeyValue],
        classOf[HFileOutputFormat2],
        hbaseConf
      )
  }

}
