package com.tp.spark.core

import org.apache.spark.{SparkConf, SparkContext, sql}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

object Ex0WordcountDF {

  val pathToFile = "data/wordcount.txt"

  def loadData(): DataFrame = {
    // create spark configuration and spark context: the Spark context is the entry point in Spark.
    // It represents the connexion to Spark and it is the place where you can configure the common properties
    // like the app name, the master url, memories allocation...
    val conf = new SparkConf()
                        .setAppName("Wordcount")
                        .setMaster("local[*]") // here local mode. And * means you will use as much as you have cores.

    val ss = SparkSession.builder()
      .config(conf)
      .getOrCreate()

    ss.read.csv(pathToFile).toDF("line")
  }

  /**
   *  Now count how much each word appears!
   */
  def wordcountDF() : DataFrame = {
    val df = loadData();
    val wordsDF = df.explode("line","word")((line: String) => line.split(" "))
    wordsDF.groupBy("word").count()
  }

  /**
   *  Now keep the word which appear strictly more than 4 times!
   */
  def filterOnWordcountDF() : DataFrame = {
    val df = wordcountDF();
    df.where("word");
  }

}
