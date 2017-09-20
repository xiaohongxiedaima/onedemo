package com.xiaohongxiedaima.demo.zipkin

import brave.opentracing.BraveTracer

/**
  * Created by liusheng on 17-9-11.
  */
object Test {
  def main(args: Array[String]): Unit = {
    val tracing = AppTracing.init()

    val tracer = BraveTracer.create(tracing)

    val span = tracer.buildSpan("o_1").ignoreActiveSpan().startActive()

    span.deactivate()
  }
}
