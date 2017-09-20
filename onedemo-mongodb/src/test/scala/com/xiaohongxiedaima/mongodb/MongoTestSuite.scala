package com.xiaohongxiedaima.mongodb

import java.util.concurrent.TimeUnit

import org.mongodb.scala.bson.{BsonDocument, BsonNull}
import org.mongodb.scala.{MongoClient, Observable, Observer}
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.equal
import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer

/**
  * Created by liusheng on 17-7-28.
  */
class MongoTestSuite extends FunSuite {

  val mongoClient = MongoClient("mongodb://192.168.1.69")
  val mongoDatabase = mongoClient.getDatabase("dmp_report")
  val collection = mongoDatabase.getCollection[BsonDocument]("dmp_ad_exposure")

  test("find") {
    import com.xiaohongxiedaima.mongodb.Helpers._

    collection.find().results().foreach(doc => println(doc.toJson()))
  }

  test("filters") {
    import org.mongodb.scala.model.Filters._
    import com.xiaohongxiedaima.mongodb.Helpers._

    val filters = equal("name", "MongoDB")

    collection.find(filters).results().foreach(doc => println(doc.toJson()))
  }

  test("agg") {
    import org.mongodb.scala.model.Filters._
    import org.mongodb.scala.model.Aggregates._
    import org.mongodb.scala.model.Accumulators._
    import org.mongodb.scala.model.Sorts._
    import com.xiaohongxiedaima.mongodb.Helpers._

    val stages = ArrayBuffer[Bson]()
    val filterStage = filter(and(notEqual("aderid", null), equal("groupid", "1bm9ua3swno8")))

    val groupStage = group(Document("{'d': '$d', 'h': '$h'}"), sum("exposureSum", "$exposuresum"))

    val sortStage = sort(orderBy(ascending("_id")))

    stages.append(filterStage)
    stages.append(groupStage)
    stages.append(sortStage)

    collection.aggregate[BsonDocument](stages).results().foreach(doc => {
      val _id = doc.getDocument("_id")
      val d = _id.getString("d").getValue
      val h = _id.getString("h").getValue

      val exposureSum = doc.getDouble("exposureSum").getValue.intValue()

      println((d, h, exposureSum))
    })
  }
}
