package com.xiaohongxiedaima.mongodb

import org.mongodb.scala.MongoClient

/**
  * Created by liusheng on 17-7-28.
  */
class MongoUtils {
  def getMongoClient() = {
    MongoClient("mongodb://localhost")
  }
}
