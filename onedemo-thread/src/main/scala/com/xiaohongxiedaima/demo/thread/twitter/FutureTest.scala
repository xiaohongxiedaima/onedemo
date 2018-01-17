package com.xiaohongxiedaima.demo.thread.twitter

import com.twitter.util.FuturePool
import com.twitter.util.logging.Logging

object FutureTest extends Logging {


  def main(args: Array[String]): Unit = {

    logger.info(Thread.currentThread().getName)

    val future = FuturePool.unboundedPool.apply({
      Thread.sleep(1000)
      logger.info(Thread.currentThread().getName)
      "Hello"
    })

    future.onSuccess(str => {
      logger.info(Thread.currentThread().getName)
      logger.info(str)
    })


    Thread.sleep(10000)



  }

}