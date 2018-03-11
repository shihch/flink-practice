package org.clark

import java.sql.Timestamp

sealed trait TimedEvent {
  val id: Long
  val created: Timestamp
}

case class User(id: Long, created: Timestamp, accountId: Long) extends TimedEvent
case class Account(id: Long, created: Timestamp, aType: String, fromIp: String) extends TimedEvent
case class Ticket(id: Long, created: Timestamp, accountId: Long, content: String) extends TimedEvent

case class Spammer(id:Long, alert:String)
  