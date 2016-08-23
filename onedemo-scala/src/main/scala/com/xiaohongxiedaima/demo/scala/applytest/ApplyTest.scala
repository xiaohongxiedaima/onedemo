package com.xiaohongxiedaima.demo.scala.applytest

/**
  * @author xiaohongxiedaima
  * @version 16-8-19
  * @E-mail redfishinaction@yahoo.com
  */
trait User {
  
  def name: String

}

class FreeUser(val name: String) extends User

class VipUser(val name: String) extends User

object FreeUser {

  def unapply(arg: FreeUser): Option[String] = Some(arg.name)

}

object VipUser {

  def unapply(arg: VipUser): Option[String] = None

}

object UnapplyTest {
  def main(args: Array[String]) {
    val user = new VipUser("shi")

    val result = user match {
      case FreeUser(name) => name + "san_free"
      case VipUser(name) => name + "san_vip"
    }

    result.charAt(1)
    println(result)
  }
}
