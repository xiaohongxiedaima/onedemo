package com.xiaohongxiedaima.demo.scala.objecttest

/**
  * @author xiaohongxiedaima
  * @version 16/8/28
  * @E-mail redfishinaction@yahoo.com
  */
object Margin extends Enumeration{
  val TOP,BOTTOM,LEFT,RIGHT = Value
}

object Main {
  def main(args: Array[String]) {
    import Margin._
    val margin = BOTTOM

    val marginInStr = margin match {
      case TOP => "top"
      case _ => None
    }
    println(marginInStr)

    Margin.values.foreach(println)
  }
}

