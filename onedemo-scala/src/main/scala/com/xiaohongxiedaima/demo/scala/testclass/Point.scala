package com.xiaohongxiedaima.demo.scala.testclass

/**
  * @author xiaohongxiedaima
  * @version 16-6-24
  * @E-mail redfishinaction@yahoo.com
  */
class Point(val x: Int, val y: Int) {

  def this() {
    this(0, 0)
  }

  override def toString = {
    s"{x = $x, y = $y}"
  }

}

object Point {
  def main(args: Array[String]) {

    val point = new Point
    print(point)


  }
}
