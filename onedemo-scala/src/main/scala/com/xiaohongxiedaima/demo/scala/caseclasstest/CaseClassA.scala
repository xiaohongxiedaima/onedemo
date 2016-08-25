package com.xiaohongxiedaima.demo.scala.caseclasstest

/**
  * @author xiaohongxiedaima
  * @version 16-8-24
  * @E-mail redfishinaction@yahoo.com
  */
class CaseClassA(val a:String) {

}

case class CaseClassB(val a: String) {

}

object CaseClassA {
  def apply(a:String): CaseClassA = new CaseClassA(a)
}

object Main{
  def main(args: Array[String]) {

    val a = CaseClassA("a")
    val b = CaseClassB("b")

    println(a)
    println(b)

  }
}