package com.xiaohongxiedaima.demo.scala.boundtest

/**
  * Created by xiaohong on 2016-09-14.
  */
class Person(name: String, age: Int) {
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

case class Pair[T1, T2](val key: T1, val value: T2) {

}



object BoundTest {
  def main(args: Array[String]): Unit = {
    val student = new Student("sheng", 100)
    println(student)

    val personPair = new Pair[Long, Person](1, new Person("sheng"))
    println(personPair.value.printlnPerson)

    val studentPair = new Pair[Long, Person](1, new Student("sheng")) // 将Student作为Person处理了
    println(studentPair.value.printlnPerson)

    // 型变


  }
}
