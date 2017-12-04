package com.xiaohongxiedaima.demo.scala.trytest

import scala.util.{Failure, Success, Try}

/**
  * Created by liusheng on 17-12-1.
  */

object TryTest {

  def add(a: Int, b: String): Try[Int] = {
    try {
      Success(a + Integer.valueOf(b))
    } catch {
      case t: Throwable => Failure(t)
    }
  }

  def main(args: Array[String]): Unit = {

    val a1 = add(1, "0")
    val a2 = add(1, "")

    a1.foreach(println)
    a2.foreach(println)

  }

}
