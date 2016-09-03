package com.xiaohongxiedaima.demo.scala

/**
  * @author xiaohongxiedaima
  * @version 16/8/20
  * @E-mail redfishinaction@yahoo.com
  */

trait User {
  def name(): String
  def score(): Int
}

class FreeUser(val name: String, val score: Int) extends User

class VipUser(val name: String, val score: Int) extends User

object FreeUser {
  def unapply(freeUser: FreeUser): Option[(String, Int)] = Some((freeUser.name, freeUser.score))
}

object VipUser {
  def unapply(vipUser: VipUser): Option[String] = None
}

class Join {
  def join(str: String, int: Int) = {
    str + "," + int
  }
}

object UnApplyTest {
  def main(args: Array[String]) {

    val join = new Join
    val user:User = new VipUser("shisan", 99)

    val result = user match {
      case FreeUser(name, score) => join.join(name,score)
      case VipUser(name) => join.join(name,0)
      case _ => "" + "x"
    }

    val a = 2 :: 3 :: Nil

    val b = 2 #:: 4 #:: Stream.empty

  }
}