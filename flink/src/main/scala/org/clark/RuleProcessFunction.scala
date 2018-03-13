package org.clark

import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.slf4j.LoggerFactory
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.common.state.ValueState
import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.configuration.Configuration
import collection.mutable.ListBuffer

/* this class is refactored to two classes */

class RuleProcessFunction extends ProcessWindowFunction[TimedEvent, String, Long, TimeWindow] {

  val log = LoggerFactory.getLogger(this.getClass)
  private var totalUsers: ValueState[Long] = _

  override def open(parameters: Configuration): Unit = {
    totalUsers = getRuntimeContext.getState(
      new ValueStateDescriptor[Long]("totalUsers", classOf[Long]))
  }

  override def process(key: Long, context: Context, elements: Iterable[TimedEvent], out: Collector[String]): Unit = {
    val filters = Spammy.filters
    var countUsers = 0
    val param = getRuntimeContext.getExecutionConfig.getGlobalJobParameters.asInstanceOf[ParameterTool]
    val countLimit = param.getInt("account_limit")
    val risks = ListBuffer.empty[Spammer]

    for (ev <- elements) {
      ev match {
        case account: Account =>
          val alert = Inspector.raiseAlert(account)
          if (alert != None) risks.append(alert.get)
        case user: User => countUsers += 1
        case ticket: Ticket =>
          val alert = filters(ticket)
          if (alert != None) risks.append(alert.get)
      }
    }

    val total = totalUsers.value + countUsers
    totalUsers.update(total)
    if (countUsers > countLimit) {
      risks.append(Spammer(key, s"$countUsers", "acc-win"))
    }
    if (total > countLimit) {
      risks.append(Spammer(key, s"$total", "acc-tt"))
    }

    //output the captured spams
    if (risks.size > 0) {
      val printout = risks.foldLeft(s"Account $key is risky because \n") {
        (s, spam) =>
          spam.atype match {
            case "word" =>
              s + s"  - It created a spammy ticket with '${spam.alert}' word(s) in it\n"
            case "link" =>
              s + s"  - It created a spammy ticket with '${spam.alert}' link\n"
            case "acc-win" =>
              s + s"  - Account has ${spam.alert} users, exceeds limit $countLimit\n"
            case "acc-tt" =>
              s + s"  - Account has so far ${spam.alert} total users, exceeds limit $countLimit\n"
            case "ip" =>
              s + s"  - It is created from a known bad ip: ${spam.alert}\n"
          }
      }

      out.collect(printout)
    }
  }
}



/**
 * function that keep the total user count state and emit the result from the aggregate function 
 */
class SpamReportFunction extends ProcessWindowFunction[SpamAcc, String, Long, TimeWindow] {
  private var totalUsers: ValueState[Long] = _
  private var countLimit: Long = 0
  val log = LoggerFactory.getLogger(this.getClass)

  override def open(parameters: Configuration): Unit = {
    totalUsers = getRuntimeContext.getState(
      new ValueStateDescriptor[Long]("totalUsers", classOf[Long]))

    val param = getRuntimeContext.getExecutionConfig.getGlobalJobParameters.asInstanceOf[ParameterTool]
    this.countLimit = param.getInt("account_limit")
  }

  override def process(key: Long, context: Context, elements: Iterable[SpamAcc], out: Collector[String]): Unit = {
    if (!elements.iterator.hasNext) return
    val acc = elements.iterator.next
    val total = totalUsers.value + acc.userCounts
    totalUsers.update(total)
    if (acc.userCounts > countLimit) {
      acc.spams.append(Spammer(key, s"${acc.userCounts}", "acc-win"))
    }
    if (total > countLimit) {
      acc.spams.append(Spammer(key, s"$total", "acc-tt"))
    }

    if (acc.spams.size > 0) {
      val printout = acc.spams.foldLeft(s"Account $key is risky because \n") {
        (s, spam) =>
          spam.atype match {
            case "word" =>
              s + s"  - It created a spammy ticket with '${spam.alert}' word(s) in it\n"
            case "link" =>
              s + s"  - It created a spammy ticket with '${spam.alert}' link\n"
            case "acc-win" =>
              s + s"  - Account has ${spam.alert} users, exceeds limit $countLimit\n"
            case "acc-tt" =>
              s + s"  - Account has so far ${spam.alert} total users, exceeds limit $countLimit\n"
            case "ip" =>
              s + s"  - It is created from a known bad ip: ${spam.alert}\n"
          }
      }

      out.collect(printout)
    }

  }
}

class SpamAcc {
  val spams = ListBuffer.empty[Spammer]
  var userCounts = 0
}

/**
 * functions that capture spams and window user count
 */
class FilterFunction extends AggregateFunction[TimedEvent, SpamAcc, SpamAcc] {
  import Spammy._
  //ticket filter rule
  val filters = any(
    Seq(domainFilter("bit.ly"), wordFilter("Apple"), wordFilter("Paypal"),
      every(Seq(wordFilter("reset"), wordFilter("password"))) { _ + _ }))

  override def createAccumulator() = new SpamAcc

  override def add(ev: TimedEvent, accumulator: SpamAcc) = {
    ev match {
      case account: Account =>
        val alert = Inspector.raiseAlert(account)
        if (alert != None) accumulator.spams.append(alert.get)
      case user: User => accumulator.userCounts += 1
      case ticket: Ticket =>
        val alert = filters(ticket)
        if (alert != None) accumulator.spams.append(alert.get)
    }
    accumulator
  }

  override def merge(a: SpamAcc, b: SpamAcc) = {
    a.spams ++= b.spams
    a.userCounts += b.userCounts
    a
  }

  override def getResult(accumulator: SpamAcc) = accumulator
}


