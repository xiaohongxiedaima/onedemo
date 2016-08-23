package com.xiaohongxiedaima.demo.scala.matchtest

/**
  * @author xiaohongxiedaima
  * @version 16-8-19
  * @E-mail redfishinaction@yahoo.com
  */

object MatchResultTest {

  def main(args: Array[String]) {
    val a = 1 + 2 match {
      case 2 => 4
      case 3 => 6
      case _ => 8
    }

    val s = "hello" + "world" match {
      case "world" => "world"
      case "hello" => "hello"
      case _ => "x"
    }

    println(a)
    println(s)
  }
}