package org.clark

import org.apache.flink.streaming.api.scala._

object FlinkStarter {
  
  val accounts_threshold = 20
  
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    // get input data by connecting to the socket
    val text = env.socketTextStream("localhost", 9000, '\n')
    
    val parsedStream = text.flatMap { line =>
      JsonParse.parseEvent(line)
    }
    
    val ticketStream = parsedStream.filter{ev=>ev.isInstanceOf[Ticket]}.map { e => e.asInstanceOf[Ticket] }    
    val userStream = parsedStream.filter(ev=>ev.isInstanceOf[User]).map ( e => e.asInstanceOf[User] )
    val accountStream = parsedStream.filter(ev=>ev.isInstanceOf[Account]).map ( e => e.asInstanceOf[Account] )
    
    val badUsers=userStream.map(u=>(u.accountId,1)).keyBy(0).sum(1).filter(_._2>accounts_threshold)
    
    val badTickets = ticketStream.flatMap { t => Spammy.filters(t) }
    
    val badAcct=accountStream.flatMap(acct=>Inspector.raiseAlert(acct))
      
    badAcct.print().setParallelism(1)
    
    env.execute("Flink Starter")
    

    //import Spammy._
    //Spammy(wordFilter(""))
  }
}