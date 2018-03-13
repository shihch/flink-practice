package org.clark

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import scala.collection.Iterable
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.api.java.utils.ParameterTool
import java.util.concurrent.TimeUnit
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.runtime.state.memory.MemoryStateBackend

object FlinkStarter {

  def main(args: Array[String]): Unit = {

    val parameters = ParameterTool.fromPropertiesFile(getClass.getResourceAsStream("/job.properties"))
    //println(parameters.getInt("account_limit"))

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.getConfig.setGlobalJobParameters(parameters)

    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setStateBackend(new MemoryStateBackend())

    // get input data by connecting to the socket
    val text = env.socketTextStream(parameters.get("host"), parameters.getInt("port"), '\n')

    val parsedStream = text.flatMap { line =>
      JsonParse.parseEvent(line)
    }

    val maxDelay = parameters.getLong("watermark_maxDelay")
    env.getConfig.setAutoWatermarkInterval(parameters.getLong("watermark_check_interval"))
    
    val withTsWm =parsedStream.assignTimestampsAndWatermarks(new PeriodicWM(maxDelay))

    val tWindow = TumblingEventTimeWindows.of(Time.of(
      parameters.getLong("twindow_time"), TimeUnit.valueOf(parameters.get("twindow_time_unit"))))
    val accounts = withTsWm.keyBy { ev =>
      ev match {
        case e: Account => e.id
        case e: User => e.accountId
        case e: Ticket => e.accountId
      }
    }.window(tWindow).aggregate(new FilterFunction, new SpamReportFunction)
    //.process(new RuleProcessFunction)

    accounts.print().setParallelism(1)

    env.execute("Flink Starter")

  }

  class PunctualWM(maxDelay:Long) extends AssignerWithPunctuatedWatermarks[TimedEvent] {
    override def extractTimestamp(element: TimedEvent, previousElementTimestamp: Long): Long = element.created
    override def checkAndGetNextWatermark(lastElement: TimedEvent, extractedTimestamp: Long): Watermark = {
      new Watermark(extractedTimestamp - maxDelay)
    }
  }
  
  class PeriodicWM(maxDelay:Long) extends AssignerWithPeriodicWatermarks[TimedEvent] {
    var currentMaxTimestamp:Long =0
    override def getCurrentWatermark:Watermark = new Watermark(currentMaxTimestamp - maxDelay)
    override def extractTimestamp(element: TimedEvent, previousElementTimestamp: Long): Long = {
      val timestamp=element.created
      currentMaxTimestamp = Math.max(timestamp, currentMaxTimestamp)
      timestamp
    }
  }

}

