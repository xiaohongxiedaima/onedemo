package com.xiaohongxiedaima.demo.scala

import scala.collection.mutable.ArrayBuffer

/**
  * @author xiaohongxiedaima
  * @version 16-8-25
  * @E-mail redfishinaction@yahoo.com
  */
object ArrayBufferTest {

  def main(args: Array[String]) {
    val ab = ArrayBuffer[Int](1,23)

    val ab1 = ab += 1
    println(ab, ab1)

    val ab2 = ab.+=(1,2,3)
    println(ab, ab2)

    val ab3 = ab.++=(Array(12,3))
    println(ab, ab3)

  }

}
