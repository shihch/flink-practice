package org.clark

import scala.util.Try
import scala.util.Success

object Inspector {
  
  /**
   * Fake bad ip based on sum of its digits
   */
  def checkBadIP(ip:String):Boolean = {
    val ips=ip.split('.')
    val isBad = Try{
      ips.map { digit => digit.toInt }.sum
    }
    isBad match {
      case Success(v) => v % 3 ==0
      case _ => true
    }
  }
  
  def raiseAlert(acct:Account):Option[Spammer] = {
    if (checkBadIP(acct.fromIp))
      Some(Spammer(acct.id,acct.fromIp,"ip"))
    else
      None
  }
}