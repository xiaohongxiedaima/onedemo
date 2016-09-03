package com.xiaohongxiedaima.demo.scala

/**
  * @author xiaohongxiedaima
  * @version 16/8/21
  * @E-mail redfishinaction@yahoo.com
  */
object ArrayTest {
  def main(args: Array[String]) {
    val strs = "hello world !!!"
    val result = strs.split(" ").forall( x => {
      println(x)
      x.isEmpty
    })
    println(result)
  }
}
