package com.xiaohongxiedaima.demo.scala

/**
  * @author xiaohongxiedaima
  * @version 16-6-23
  * @E-mail redfishinaction@yahoo.com
  */
object Function {

  def main(args: Array[String]) {
    val reduce = (x: AnyVal, y: AnyVal) => {
      val result = x.asInstanceOf[Int] + y.asInstanceOf[Int]
      println(result)
      result
    }

    val seq = Array(1,3,4,5)
    seq.reduce(reduce)

    seq.reduce((x,y) => {
      x + y
    })

    seq.reduce{
      (x,y) => {
        x + y
      }
    }

    seq reduce {
      (x, y) => {
        x + y
      }
    }

  }

}
