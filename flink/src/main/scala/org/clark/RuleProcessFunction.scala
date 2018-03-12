package org.clark


import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.slf4j.LoggerFactory

class RuleProcessFunction extends ProcessWindowFunction[TimedEvent, String, Long, TimeWindow] {
  import collection.mutable.ListBuffer
  val log=LoggerFactory.getLogger(this.getClass)
    
  override def process(key: Long, context: Context, elements: Iterable[TimedEvent], out: Collector[String]): Unit = {
    val filters = Spammy.filters
    var countUsers = 0
    val countLimit = 20
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
    if (countUsers > countLimit) {
      risks.append(Spammer(key,s"Account has $countUsers users, more than $countLimit"))
    }
    if (risks.size > 0){  
      val printout=risks.foldLeft(s"Account $key is risky because \n"){
        (s,r) => s + s"\t- ${r.alert} \n"
      }
    
      out.collect(printout)
    }
  }
  
 
}