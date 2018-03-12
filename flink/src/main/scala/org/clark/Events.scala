package org.clark

import java.sql.Timestamp

sealed trait TimedEvent {
  val id: Long
  val created: Long
}

case class User(id: Long, created: Long, accountId: Long) extends TimedEvent
case class Account(id: Long, created: Long, aType: String, fromIp: String) extends TimedEvent
case class Ticket(id: Long, created: Long, accountId: Long, content: String) extends TimedEvent

case class Spammer(id:Long, alert:String, atype:String="word") {
  def +(sp:Spammer):Spammer = {
    if(id==sp.id)
      Spammer(id, s"$alert and ${sp.alert}",atype)
    else this
  }
}
  