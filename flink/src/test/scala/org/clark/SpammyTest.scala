package org.clark

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.sql.Timestamp

@RunWith(classOf[JUnitRunner])
class SpammyTest extends FunSuite {

  import Spammy._
  test("test spammy word") {
    assert(withWord("The Apple campus is new.")("Apple"))
    assert(withWord("Apple campus is good.")("Apple"))
    assert(!withWord("Orange campus is bad.")("Apple"))
    assert(withWord("Orange campus is not Apple.2")("Apple"))
    assert(withWord("Orange campus is not Apple 2.")("Apple 2"))
    assert(!withWord("欢迎来到zendesk and welcome!")("zendesk"))
    assert(!withWord("It is my pleasure -Apple")("Apple"))
  }

  test("test spammy domain") {

    assert(withDomain("dkfdalhttp://bit.ly/gbajlfb")("bit.ly"))
    assert(withDomain("dkfdal http://bit.ly.")("bit.ly"))
    assert(withDomain("dkfdalhttps://www.bit.ly/gbajlfb")("bit.ly"))
    assert(withDomain("http://bit.ly/gba#$jlfb")("bit.ly"))
    assert(!withDomain("abc http://sub.bit.ly/gba#$jlfb xyz")("bit.ly"))
    assert(withDomain("abc http://bit.ly/gba#$jlfb xyz")("bit.ly"))

  }
  
  test("combined any filter") {
    val filter=Spammy(
      any(Seq(wordFilter("xyz"),domainFilter("goo.le")))
    )
    
    var sp=filter(Ticket(1, 31341000, 3, "test ticket with link xyz."))
    assert(!sp.isEmpty)
  }

}