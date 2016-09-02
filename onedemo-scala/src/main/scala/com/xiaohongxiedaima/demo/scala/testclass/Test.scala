package com.xiaohongxiedaima.demo.scala.testclass

/**
  * @author xiaohongxiedaima
  * @version 16-9-1
  * @E-mail redfishinaction@yahoo.com
  */
class Test(val className: String) {
  def query[T](sql: String, clazz: Class[T]): List[T] = {
    List[T]()
  }
}

