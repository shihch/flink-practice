package org.clark

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.sql.Timestamp

@RunWith(classOf[JUnitRunner])
class JasonParseTest extends FunSuite {
  test("invalid json cases") {
    var s = ""
    var parsed = JsonParse.parseEvent(s)
    assert(parsed == None)

    s = "{}"
    parsed = JsonParse.parseEvent(s)
    assert(parsed == None)

    s = "x"
    parsed = JsonParse.parseEvent(s)
    assert(parsed == None)

    s = """{"data_type":"user"}"""
    parsed = JsonParse.parseEvent(s)
    assert(parsed == None)
    /*    
      val tests = """
{ "data_type": "ticket", "id": 1, "account_id": 1, "content": "Welcome to zendesk", "created_at": 1517937463 }
{ "data_type": "user", "id": 124, "account_id": 3, "created_at": 1517910990 }
{ "data_type":"account", "id": 10, "type": "Customer", "created_at": 1517861251, "created_from_ip": "103.206.130.182" }
  """
  
  tests.split("\n").toList.foreach { line => 
    println(JsonParse.parseEvent(line))
  }*/

  }

  test("parse to proper object") {
    var s = """{ "data_type": "ticket", "id": 1, "account_id": 1, "content": "Welcome to zendesk", "created_at": 1517937463 }"""
    var parsed = JsonParse.parseEvent(s)
    assert(parsed.get.isInstanceOf[Ticket])

    s = """{ "data_type": "use", "id": 124, "account_id": 3, "created_at": 1517910990 }"""
    parsed = JsonParse.parseEvent(s)
    assert(parsed == None)

    s = """{ "data_type": "user", "id": 124, "account_id": 3, "created_at": 1517910990 }"""
    parsed = JsonParse.parseEvent(s)
    assert(parsed.get.isInstanceOf[User])

    s = """{ "data_type":"account", "id": 10, "type": "Customer", "created_at": 1517861251, "created_from_ip": "103.206.130.182" }"""
    parsed = JsonParse.parseEvent(s)
    assert(parsed.get.isInstanceOf[Account])
  }
}