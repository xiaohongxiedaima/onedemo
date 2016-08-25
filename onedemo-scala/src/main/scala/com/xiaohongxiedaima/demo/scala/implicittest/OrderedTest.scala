package com.xiaohongxiedaima.demo.scala.implicittest

import scala.util.Sorting

/**
  * @author xiaohongxiedaima
  * @version 16-8-25
  * @E-mail redfishinaction@yahoo.com
  */
case class OrderedTest(val name: String, val age: Int) {

}

object OrderedTest {

  implicit def orderedTestOrdered = (orderedTest: OrderedTest) => {
    new Ordered[OrderedTest] {
      override def compare(that: OrderedTest): Int = {
        if (orderedTest.name > that.name) 1 else if (orderedTest.name < that.name) -1 else 0
      }
    }
  }

  def main(args: Array[String]) {
    val arr = Array(OrderedTest("shi", 11), OrderedTest("san", 13), OrderedTest("liu", 12))
    Sorting.quickSort(arr)
    arr.map {
      case OrderedTest(name, age) => {
        s"($name,$age)"
      }
    }
    arr.foreach(println(_))
  }
}