package com.zendesk

import org.apache.spark.sql.SparkSession

object SparkRulesEngine {
  def main(args: Array[String]) {
    val spark = SparkSession.builder
      .master("local[*]")
      .appName("SparkRulesEngine")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    val lines = spark.sparkContext.parallelize(Seq(
      "hello world",
      "hello zendesk",
      "zendesk is a great place to work"
    ))

    val counts = lines
      .flatMap(_.split(" "))
      .map(_ -> 1)
      .reduceByKey(_ + _)

    counts.foreach(println)
  }
}
