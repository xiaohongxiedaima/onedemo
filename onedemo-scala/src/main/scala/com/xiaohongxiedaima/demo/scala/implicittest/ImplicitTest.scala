package com.xiaohongxiedaima.demo.scala.implicittest

import scala.math._
/**
  * @author xiaohongxiedaima
  * @version 16-8-25
  * @E-mail redfishinaction@yahoo.com
  */
object ImplicitTest {

  // 何时执行隐式转换
  // 1. 对象访问不存在的成员
  // 2. 表达式的类型和预期的类型不同
  // 3. 对象调用某个方法，而方法参数声明与传入参数不匹配
  def main(args: Array[String]) {

    implicit def intToTestClass(a: Int) = TestClass(a)
    val a = 1
    // 访问对象不存在的成员
    val b = a.increment.increment
    println(a)
    println(b)


    implicit def testClassToDouble(a: TestClass) = a.a.toDouble
    val c = TestClass(4)
    // 表达式的类型和预期的类型不同
    val d = sqrt(c)
    println(c)
    println(d)

    implicit def testClassToTestClass2(a: TestClass) = new TestClass2(a.a)
    val e = TestClass(1)
    // 对象调用某方法，而方法参数声明与传入参数不匹配
    val f = e.increment(2)
    println(e, f)

  }

}

case class TestClass(val a:Int) {
  def increment: Int = a + 1
}

case class TestClass2(val b: Int) {
  def increment(step: Int): Int = b + step
}

