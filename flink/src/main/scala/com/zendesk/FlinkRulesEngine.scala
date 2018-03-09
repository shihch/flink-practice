package com.zendesk

import org.apache.flink.streaming.api.scala._

object FlinkRulesEngine {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val text = env.fromElements("hello world", "hello zendesk", "zendesk is a great place to work")

    val windowCounts = text
      .flatMap { w => w.split("\\s") }
      .map { w => WordWithCount(w, 1) }
      .keyBy("word")
      .sum("count")
    windowCounts.print().setParallelism(1)

    env.execute("FlinkRulesEngine")
  }

  case class WordWithCount(word: String, count: Long)

}
