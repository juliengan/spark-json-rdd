package com.tp.spark.core

import com.tp.spark.core.Ex2TweetMining.{loadData, mentionOnTweet}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd._
import com.tp.spark.utils.TweetUtils
import com.tp.spark.utils.TweetUtils._

/**
 *  We still use the dataset with the 8198 reduced tweets. Here an example of a tweet:
 *
 *  {"id":"572692378957430785",
 *    "user":"Srkian_nishu :)",
 *    "text":"@always_nidhi @YouTube no i dnt understand bt i loved of this mve is rocking",
 *    "place":"Orissa",
 *    "country":"India"}
 *
 *  We want to make some computations on the hashtags. It is very similar to the exercise 2
 *  - Find all the hashtags mentioned on a tweet
 *  - Count how many times each hashtag is mentioned
 *  - Find the 10 most popular Hashtag by descending order
 */
object Ex3HashTagMining {

  val pathToFile = "data/reduced-tweets.json"

  /**
   *  Load the data from the json file and return an RDD of Tweet
   */
  def loadData(): RDD[Tweet] = {
    // create spark configuration and spark context
    val conf = new SparkConf()
        .setAppName("Hashtag mining")
        .setMaster("local[*]")

    val sc = SparkContext.getOrCreate(conf)

    // Load the data and parse it into a Tweet.
    // Look at the Tweet Object in the TweetUtils class.
    sc.textFile(pathToFile)
        .mapPartitions(TweetUtils.parseFromJson(_))
  }

  /**
   *  Find all the hashtags mentioned on tweets (duplicates are allowed)
   */
  def hashtagMentionedOnTweet(): RDD[String] = {
    /*val myReg = "(^|[^#\\w])#(\\w{1,30})+\\b".r
    loadData()
      .flatMap(x => myReg.findAllIn(x.text)
        .matchData.map(_.group(0).trim -> 1))
      .reduceByKey(_ + _)
      .map{x=> println(x._1);x._1}*/
    loadData().flatMap(x=> x.text.split(" ")).filter(x => x.startsWith("#")).filter(x=> !x.endsWith("#"))

  }


  /**
   *  Count how many times each hashtag is mentioned
   */
  def countMentions(): RDD[(String, Int)] = {
    hashtagMentionedOnTweet()
    .map { hashtag => (hashtag, 1) }
      .reduceByKey(_ + _)
      .map{ x => println(x);x}
  }

  /**
   *  Find the 10 most popular Hashtags by descending order
   */
  def top10HashTags(): Array[(String, Int)] = {
    hashtagMentionedOnTweet()
      .map { person => (person, 1) }
      .reduceByKey(_ + _)
      .takeOrdered(10) {
        Ordering[ Int].reverse.on(_._2)
      }
  }

}
