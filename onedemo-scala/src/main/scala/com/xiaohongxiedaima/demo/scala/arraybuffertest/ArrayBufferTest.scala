package com.xiaohongxiedaima.demo.scala.arraybuffertest

import scala.collection.mutable.ArrayBuffer

/**
  * @author xiaohongxiedaima
  * @version 16/8/25
  * @E-mail redfishinaction@yahoo.com
  */
object ArrayBufferTest {
  def main(args: Array[String]) {
    val ab = ArrayBuffer[Int]()
    ab += 1 += (1,2,3) ++= Array[Int](4,5)
    println(s"ab: $ab")
  }
}
