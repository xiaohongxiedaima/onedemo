package com.xiaohongxiedaima.demo.scala.boundtest

/**
  * Created by liusheng on 17-12-1.
  */
object UpperBoundTest {

  def getName[T <: A](a: T) = {
    println(a.name)
  }

  def main(args: Array[String]): Unit = {

    getName(new SubA("liu", 1))

  }

}

class A(val name: String)

class SubA(name: String, val score: Int) extends A(name) {}