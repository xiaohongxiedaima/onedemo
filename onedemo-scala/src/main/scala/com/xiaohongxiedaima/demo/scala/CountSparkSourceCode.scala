package com.xiaohongxiedaima.demo.scala

import java.io.{FileInputStream, FileFilter, File}
import java.util.Scanner

/**
  * @author xiaohongxiedaima
  * @version 16/9/3
  * @E-mail redfishinaction@yahoo.com
  */
object CountSparkSourceCode {

  val path = "/Users/redfish/code/spark"
  val suffix = Seq("java", "scala")

  def main(args: Array[String]) {
    val dir = new File(path)
    val files = listFiles(dir)

    val sum = files.map(f => {
      val scanner = new Scanner(new FileInputStream(f))
      var count = 0
      while (scanner.hasNext) {
        scanner.next()
        count = count + 1
      }
      println(f.getAbsolutePath + "," + count)
      count
    }).sum

    val fileCount = files.map(file => {
      if (file.getName.endsWith("java")) {
        ("java", 1)
      } else {
        ("scala", 1)
      }
    }).groupBy(_._1).map {
      case (x,y) =>(x,y.map(_._2).sum)
    }
    println(fileCount)
    println( sum)
  }

  def listFiles(dir: File): List[File] = {

    if (dir.isFile) {
      List(dir)
    } else {
      val sub = dir.listFiles(new FileFilter {
        override def accept(file: File): Boolean = {
          file.isDirectory || suffix.exists(file.getName.endsWith(_))
        }
      })

      sub.filter(_.isFile).toList ++ sub.filter(_.isDirectory).map(listFiles(_)).flatMap(_.toList).toList
    }

  }



}
