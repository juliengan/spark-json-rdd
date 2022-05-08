package com.tp.spark.core

import com.tp.spark.utils.TweetUtils.Tweet
import org.apache.spark.{SparkContext, SparkConf}

import com.tp.spark.utils.TweetUtils

import scala.collection.Map

object Ex4InvertedIndex {

  /**
   *
   * Buildind a hashtag search engine
   *
   * The goal is to build an inverted index. An inverted is the data structure used to build search engines.
   *
   * How does it work?
   *
   * Assuming #spark is an hashtag that appears in tweet1, tweet3, tweet39.
   * The inverted index that you must return should be a Map (or HashMap) that contains a (key, value) pair as (#spark, List(tweet1,tweet3, tweet39)).
   *
   */
  def invertedIndex(): Map[String, Iterable[Tweet]] = {
    // create spark  configuration and spark context
    val conf = new SparkConf()
      .setAppName("Inverted index")
      .setMaster("local[*]")

    val sc = SparkContext.getOrCreate(conf)

    val tweets = sc.textFile("data/reduced-tweets.json")
      .mapPartitions(TweetUtils.parseFromJson(_))

    tweets.flatMap(tweet => tweet.text.split(" ")
      .filter(word => (word.startsWith("#") && !(word.endsWith("#"))))
      .map(word => (word, tweet)))
      .groupByKey()
      .collectAsMap()

  }
}
