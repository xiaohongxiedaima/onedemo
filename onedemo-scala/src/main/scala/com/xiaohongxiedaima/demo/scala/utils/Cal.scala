package com.xiaohongxiedaima.demo.scala.utils

import java.io.File

import scala.io.Source

/**
  * Created by liusheng on 17-9-14.
  */
object Cal {
  def main(args: Array[String]): Unit = {

    val dirPath = "/home/liusheng/repo/svn/01.code/dmp-ad-index/dmp-ad-index-server"

    val dir = new File(dirPath)

    println(list(dir))
  }

  def list(dir: File): Int = {
    if (dir.isDirectory) {
      dir.listFiles().map(file => {
        list(file)
      }).sum
    } else {
      if (dir.getName.endsWith(".scala") || dir.getName.endsWith(".java")) {
        Source.fromFile(dir).getLines().size
      } else 0
    }
  }
}
