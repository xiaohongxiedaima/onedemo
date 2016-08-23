package com.xiaohongxiedaima.demo.scala.matchtest

/**
  * @author xiaohongxiedaima
  * @version 16-7-28
  * @E-mail redfishinaction@yahoo.com
  */
object MatchCaseTest {

  def main(args: Array[String]) {

    // 模式匹配
    // 如果 case 后 表达式结果类型不一样 那么 match case 后的结果类型 “编译器” 并不能确认结果是那个类型
    val a = "hello"

    val b = a match {
      case "h" => "e"
      case "hello" => "world"
      case _ => None
    }
    println(b)

    // 守卫 模式匹配中的变量
    // 如果没有匹配到结果 返回的类型是 scala.runtime.BoxedUnit
    // 没有使用 _ 结果类型 "编译器" 也不能确认结果是那个类型
    val c = a match {
      case ch => if (ch == "world") {"world"}
    }
    print(c)

  }

}
