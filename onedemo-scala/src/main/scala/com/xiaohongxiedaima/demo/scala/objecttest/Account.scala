package com.xiaohongxiedaima.demo.scala.objecttest

/**
  * @author xiaohongxiedaima
  * @version 16/8/28
  * @E-mail redfishinaction@yahoo.com
  */
class Account(val uniqueKey:Int, var balance: Double) {


}

trait MyLog {
  def info(msg: String): this.type = {
    println(msg)
    this
  }
}

object Account extends MyLog{

  var unique: Int = 0

  def newUniqueKey: Int = {
    unique + 1
  }

  def apply(balance: Double): Account = {
    new Account(newUniqueKey, balance)
  }

  def main(args: Array[String]) {
    val account = Account(100)
    Account.info(account.toString)
  }

}
