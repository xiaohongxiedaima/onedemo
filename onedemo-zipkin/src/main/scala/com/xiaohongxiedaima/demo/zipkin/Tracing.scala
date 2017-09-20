package com.xiaohongxiedaima.demo.zipkin

import java.net.InetAddress

import brave.Tracing
import zipkin.reporter.AsyncReporter
import zipkin.reporter.kafka10.KafkaSender

/**
  * Created by liusheng on 17-9-11.
  */
object AppTracing {

  val zipkinKafkaAddr = "datanode13:9092,datanode14:9092,datanode15:9092,datanode16:9092,datanode17:9092"

  def init(): Tracing = {
    val sender = KafkaSender.create(zipkinKafkaAddr)
    val reporter = AsyncReporter.builder(sender).build()

    Tracing.newBuilder()
      .localServiceName(s"zipkin-test-${InetAddress.getLocalHost().getHostName()}")
      .reporter(reporter)
      .build()
  }

}
