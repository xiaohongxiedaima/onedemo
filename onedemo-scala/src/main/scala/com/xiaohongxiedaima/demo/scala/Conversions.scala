package com.xiaohongxiedaima.demo.scala

import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.JavaConversions.propertiesAsScalaMap
/**
  * @author xiaohongxiedaima
  * @version 16-6-23
  * @E-mail redfishinaction@yahoo.com
  */
object Conversions {

  def main(args: Array[String]) {

    // mapAsScalaMap
    val scalaMap: scala.collection.Map[String, String] = new java.util.TreeMap[String,String]()

    val prop: scala.collection.Map[String, String] = System.getProperties
    // mapAsJavaMap


  }

}
