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


object FlinkStarter {

  val accounts_threshold = 20

  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    // get input data by connecting to the socket
    val text = env.socketTextStream("localhost", 9000, '\n')
    
    val parsedStream = text.flatMap { line =>
      JsonParse.parseEvent(line)
    }
    
    val maxDelay = 8000
    val withTsWm =
      parsedStream.
        assignTimestampsAndWatermarks(
          new AssignerWithPunctuatedWatermarks[TimedEvent] {
            
            override def extractTimestamp(element: TimedEvent, previousElementTimestamp: Long): Long = element.created
            
            override def checkAndGetNextWatermark(lastElement: TimedEvent, extractedTimestamp: Long): Watermark = {
              new Watermark(extractedTimestamp - maxDelay)
            }
          })

    val accounts = withTsWm.keyBy { ev =>
      ev match {
        case e: Account => e.id
        case e: User => e.accountId
        case e: Ticket => e.accountId
      }
    }.window(TumblingEventTimeWindows.of(Time.minutes(3))).process(new RuleProcessFunction)
    
    /*
    val ticketStream = parsedStream.filter { ev => ev.isInstanceOf[Ticket] }.map { e => e.asInstanceOf[Ticket] }
    val userStream = parsedStream.filter(ev => ev.isInstanceOf[User]).map(e => e.asInstanceOf[User])
    val accountStream = parsedStream.filter(ev => ev.isInstanceOf[Account]).map(e => e.asInstanceOf[Account])

    val badUsers = userStream.map(u => (u.accountId, 1)).keyBy(0).sum(1) //.filter(_._2>accounts_threshold)

    val badTickets = ticketStream.flatMap { t => Spammy.filters(t) }

    val badAcct = accountStream.flatMap(acct => Inspector.raiseAlert(acct))
    */

    accounts.print().setParallelism(1)

    env.execute("Flink Starter")

  }



}

