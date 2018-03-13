package org.clark

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.sql.Timestamp

@RunWith(classOf[JUnitRunner])
class FilterFunctionTest extends FunSuite{
  
  val ff=new FilterFunction
  
  test("test adding event to the aggregator"){
    val acc=new SpamAcc
    val ev = new User(1,34524523,12)
    ff.add(ev, acc)
    assert(acc.userCounts ==1)
    assert(acc.spams.isEmpty)
    
    ff.add(new Ticket(2,4234525,4,"apple has a new campus."),acc)
    assert(acc.spams.size==1)
  }
  
  test("") {
    
  }
}