package com.xiaohongxiedaima.demo.scala.traittest

/**
  * @author xiaohongxiedaima
  * @version 16-8-17
  * @E-mail redfishinaction@yahoo.com
  */
trait TraitFieldTest {
  val a = 1
  val b: Int = 2
  var c: Int
  var d = 4
}

class TestClass1 extends TraitFieldTest{
  var c: Int = 3

  println(s"$a,$b,$c,$d")
}

object TestClass1 {
  def main(args: Array[String]) {
    val testClass1 = new TestClass1
  }
}
