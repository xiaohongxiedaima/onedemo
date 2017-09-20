package com.xiaohongxiedaima.demo.spark

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by liusheng on 2017-03-12.
  */
object Sum {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Simple Application")
    conf.setMaster("local[*]")
    val sc = new SparkContext(conf)
    val sum = sc.makeRDD(args.map(_.toInt)).sum()
    println(sum)
  }

}
