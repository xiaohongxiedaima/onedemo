package com.xiaohongxiedaima.demo.scala

/**
  * @author xiaohongxiedaima
  * @version 16-7-24
  * @E-mail redfishinaction@yahoo.com
  */
object ListToMap {

  def main(args: Array[String]) {
    val a = Array(
      (1,(2,3)),
      (2,(3,4))
    )

    val map = a.toMap[Int,(Int,Int)]
    println(map)

    val arrInStr = Array("", "").mkString(" ")
    println("s" + arrInStr + "e")
    println("s" + arrInStr.trim + "e")


  }

}
