package com.xiaohongxiedaima.demo.scala.traittest

/**
  * @author xiaohongxiedaima
  * @version 16-8-16
  * @E-mail redfishinaction@yahoo.com
  */

trait Log {

  def log(msg: String)

}

trait FileLog extends Log {

  def log(msg:String) = {
    println("[FileLog]" + msg)
  }

}

trait ConsoleLog extends Log {

  def log(msg:String) = {
    println("[ConsoleLog]" + msg)
  }

}

/**
  * 使用 【with ConsoleLog】 会存在多重继承问题
  * 需要重写 “混入的 trait ” 的共同的方法
  */

class TraitTest1 extends FileLog with ConsoleLog{

  override def log(msg: String): Unit = super.log(msg)

}

object TraitTest1 {
  def main(args: Array[String]) {
    val traitTest1 = new TraitTest1
    traitTest1.log("Trait Test1 Log")
  }
}

