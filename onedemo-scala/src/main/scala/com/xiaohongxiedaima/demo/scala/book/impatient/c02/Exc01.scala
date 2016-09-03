package com.xiaohongxiedaima.demo.scala.book.impatient.c02

/**
  * @author xiaohongxiedaima
  * @version 16-9-2
  * @E-mail redfishinaction@yahoo.com
  */
object Exc01 {
  def m(a: Int): Int = {
    if (a > 0) 1 else if (a < 0) -1 else 0
  }

  def main(args: Array[String]) {
    println(m(99))
  }
}


object Exc02 {
  def main(args: Array[String]) {
    val a = {

    }
    // scala.runtime.BoxedUnit
    println(a.getClass.getName)
  }
}

object Exc03 {
  def main(args: Array[String]) {
    var x:Unit = ()
    var y: Int = 1
    x = y = 1

    println(x , y)
  }
}

object Exc04 {
  def main(args: Array[String]) {
    val n = 10
    for (i <- (0 to n).reverse) {
      println(i)
    }

    val a = for (i <- 0 to n) yield (n - i)
    a.foreach(println)
  }

}

object Exc05 {
  def countdown(n: Int): Unit = {
    if (n == 0) {
      return
    } else {
      println(n)
      countdown(n - 1)
    }
  }

  def main(args: Array[String]) {
    countdown(10)
  }
}

object Exc06 {
  def calUnicodeOfString(str: String): Long = {
    if (str.size > 0) {
      str.head.toLong * calUnicodeOfString(str.tail)
    } else {
      1
    }
  }

  def main(args: Array[String]) {
    println(calUnicodeOfString("Hello"))
  }
}

object Exc10 {
  def main(args: Array[String]) {

  }
}