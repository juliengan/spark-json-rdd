package com.tp.spark.core

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.rdd._
import com.tp.spark.utils.TweetUtils._

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

object Ex1UserMiningDF {

  val pathToFile = "data/reduced-tweets.json"

  def loadDataDF(): DataFrame = {
    // create spark configuration and spark context: the Spark context is the entry point in Spark.
    // It represents the connexion to Spark and it is the place where you can configure the common properties
    // like the app name, the master url, memories allocation...
    val conf = new SparkConf()
                        .setAppName("User mining")
                        .setMaster("local[*]") // here local mode. And * means you will use as much as you have cores.

    val ss = SparkSession.builder()
      .config(conf)
      .getOrCreate()

    ss.read.json(pathToFile)
  }

  /**
   *   For each user return all his tweets
   */
  def tweetsByUserDF(): DataFrame = {
    loadDataDF().groupBy("user").agg(col("user"))//???
  }

  /**
   *  Compute the number of tweets by user
   */
  def tweetByUserNumberDF(): DataFrame = {
    loadDataDF().groupBy(col("user")).count()//???
  }


  /**
   *  Top 10 twitterers
   */
  def topTenTwitterersDF(): DataFrame = {
    tweetByUserNumberDF().orderBy(desc("count")).limit(10)//???
  }


}
