package org.clark

import cats.syntax.either._
import io.circe._, io.circe.parser._
import io.circe.Encoder, io.circe.syntax._, io.circe.generic.semiauto._
import org.slf4j._
import java.sql.Timestamp

object JsonParse {
  
  val log=LoggerFactory.getLogger(this.getClass)
  
  def parseEvent(s:String):Option[TimedEvent] = {
    val js=parse(s)
    if (js.isLeft) {
      log.warn(s"json parsing fail due to: ${js.left.get.message}")
      return None
    }
    val parsed=for {
      json <- parse(s)
      cursor = json.hcursor
      dataType <- cursor.downField("data_type").as[String]
      id <- cursor.downField("id").as[Long]
      time <- cursor.downField("created_at").as[Long]
      created = new Timestamp(time)
    } yield {
      val obj=dataType match {
        case "user" => 
          for (aid<-cursor.downField("account_id").as[Long]) yield Some(User(id,created,aid))
        case "account" =>
          for {
            aType<-cursor.downField("type").as[String]
            fromIp<-cursor.downField("created_from_ip").as[String]
          } yield Some(Account(id,created,aType,fromIp))
        case "ticket" =>
          for {
            aid<-cursor.downField("account_id").as[Long]
            content<-cursor.downField("content").as[String]
          } yield Some(Ticket(id,created,aid,content))
        case _ => Left(DecodingFailure("unknown type",List.empty))
      }
      obj
      
    }
    
    parsed.joinRight match {
      case Left(ex) =>
        log.warn(s"json parsing fail at fields level: ${ex}")
        None
      case Right(obj) => obj
    }

  }
  
}