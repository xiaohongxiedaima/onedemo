package com.xiaohongxiedaima.demo.scala.boundtest

/**
  * Created by xiaohong on 2016-09-14.
  */
class Person(val name: String, val age: Int) {
  def this(name: String) {
    this(name, 0)
  }

  def printlnPerson = s"$name,$age"
}

// 只有子类的主构造器能调用父类的构造器
class Student(name: String, age: Int, score: Int) extends Person(name) {
  // 辅助构造器只能调用本类的主构造器或辅助构造器
  def this(name: String, score: Int) {
    this(name, score, 0)
  }

  def this(name: String) {
    this(name, 0, 0)
  }

  def printlnStudent = s"$name,$age,$score"
}

object BoundTest {

  def getName[T <: Person](person: Person): String = {

    person.name

  }

  def main(args: Array[String]): Unit = {

    val student = new Student("liu")
    println(getName(student))

  }
}
