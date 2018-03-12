package org.clark

import org.slf4j.LoggerFactory

trait FilterComposer {

  def any[A,B](predicates: =>Seq[A => Option[B]]):A=>Option[B] =
    a => {     
      val stream=predicates.toStream.flatMap(f=>f(a))
      if (stream.isEmpty) None
      else Option(stream.head)
    }
  def every[A,B](predicates: =>Seq[A => Option[B]])(reducer: =>(B,B)=>B):A=>Option[B] =
    a => {
      val all=predicates.flatMap(f=>f(a))
      if (all.size == predicates.size) Some(all.reduce(reducer))
      else None
    }
}

object Spammy extends FilterComposer {
  type TicketFilter = Ticket => Option[Spammer] 
  
  val log=LoggerFactory.getLogger(this.getClass)
    
  def withWord(s:String)(word:String):Boolean = {
    //note: ?<= look behind, ?= look ahead
    val pattern = "(?<=^|\\s)"+word+"(?=\\s|$|\\.)"    
    val regex = pattern.r
    
    regex.findFirstIn(s) match {
      case Some(w) => 
        //println(s"'$w' matches in the string: $s")
        true
      case _ => false
    }
  }

  
  def withDomain(s:String)(domain:String) = {
    val pattern="(?<=http(s)?:\\/\\/)(www\\.)?"+domain+"(?=\\/|\\s|$|\\.)"
    val regex = pattern.r
    regex.findFirstIn(s) match {
      case Some(d) => 
        //println(s"'$d' link in the string: $s")
        true
      case _ => false
    }
  }
  
  
  def wordFilter: String=>TicketFilter = {word =>
    t => {
      if (withWord(t.content)(word))
        Some(Spammer(t.accountId,word))
      else None
    }
      
  }
  
  def domainFilter: String=>TicketFilter = {domain =>
    t => {
      if (withDomain(t.content)(domain))
        Some(Spammer(t.accountId,domain,"link"))
      else None
    }
  }
  
  val filters=any(
      Seq(domainFilter("bit.ly"), wordFilter("Apple"), wordFilter("Paypal"), 
          every(Seq(wordFilter("reset"), wordFilter("password"))){_+_}
      )
  )
  
  val unit:TicketFilter = t=>None
  
  
  def apply(body: =>TicketFilter):TicketFilter = body
    

}


