package com.xiaohongxiedaima.demo.scala.utils

import java.io.File

import scala.io.Source

/**
  * Created by liusheng on 17-9-14.
  */
object Cal {
  def main(args: Array[String]): Unit = {

    val dirPath = "/home/liusheng/repo/git/github/netty"

    val dir = new File(dirPath)

  }

  def list(dir: File, count: Integer):Unit = {
    dir.listFiles().foreach(file => {

      if (file.isDirectory) {
        list(file, count)
      } else {

        if (file.getPath.endsWith(".java")) {
          Source.fromFile(file).getLines().size
        }

      }

    })
  }
}
