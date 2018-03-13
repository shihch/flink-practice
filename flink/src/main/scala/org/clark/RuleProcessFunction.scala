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

class RuleProcessFunction extends ProcessWindowFunction[TimedEvent, String, Long, TimeWindow] {
  import collection.mutable.ListBuffer
  val log=LoggerFactory.getLogger(this.getClass)
  private var totalUsers:ValueState[Long] = _
  
  override def open(parameters: Configuration): Unit = {
    totalUsers = getRuntimeContext.getState(
      new ValueStateDescriptor[Long]("totalUsers", classOf[Long])
    )
  }
    
  override def process(key: Long, context: Context, elements: Iterable[TimedEvent], out: Collector[String]): Unit = {
    val filters = Spammy.filters
    var countUsers = 0
    val param=getRuntimeContext.getExecutionConfig.getGlobalJobParameters.asInstanceOf[ParameterTool]
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
      risks.append(Spammer(key,s"$countUsers","acc-win"))
    }
    if (total > countLimit) {
      risks.append(Spammer(key,s"$total","acc-tt"))
    }
    
    //output the captured spams
    if (risks.size > 0){  
      val printout=risks.foldLeft(s"Account $key is risky because \n"){
        (s,spam) => spam.atype match {
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
  
 // class FilterFunction extends AggregateFunction[]
   
}

