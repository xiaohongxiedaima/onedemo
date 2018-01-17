package com.xiaohongxiedaima.demo.thread.twitter

import com.twitter.util._

/**
  * Created by liusheng on 18-1-4.
  */
object FutureResultTest {


  def main(args: Array[String]): Unit = {

    implicit val timer = new ScheduledThreadPoolTimer()

    1.to(1000).foreach(_ => {
      val response = FuturePool.unboundedPool.apply({
        val start = System.currentTimeMillis()

        println(s"1: ${Thread.currentThread().getName}")
        val f = FuturePool.unboundedPool.apply({
          println(s"2: ${Thread.currentThread().getName}")
          Thread.sleep(30l)
          println("interrupted flag")
          "hello world"
        }).within(Duration.fromMilliseconds(10l))
          .rescue({
            case t: Throwable => {
              println(t)
              Future("hello error")
            }
          })
          .onSuccess(str => println(s"success: ${str}"))
          .onFailure(str => println(s"fail: ${str}"))

        Await.result(f)
        println(s"cost: ${System.currentTimeMillis() - start}")
      })
      Await.result(response)
    })

  }

}
