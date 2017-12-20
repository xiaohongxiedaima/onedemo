package com.xiaohongxiedaima.demo.scala

import scala.util.Random

/**
  * Created by liusheng on 17-12-20.
  */
object ListTest {

  def main(args: Array[String]): Unit = {
    case class A(id: String, price: Long)

    val list = List[A](A("1", 2), A("2", 2), A("3", 1), A("4", 3))

    val f = (a1: A, a2:A) => {
      val (p1, p2) = (a1.price, a2.price)
      if (p1 < p2) {
        false
      } else if (p1 > p2) {
        true
      } else {
        // 广告价可能一样 随机返回某个广告
        val random = Random.nextInt(2)
        if (random == 0) true else false
      }
    }

    val l1 = list.sortWith(f)
//    val l2 = list.sortWith(f)
//    val l3 = list.sortWith(f)
//    val l4 = list.sortWith(f)

    println(l1)
//    println(l2)
//    println(l3)
//    println(l4)
  }


}
