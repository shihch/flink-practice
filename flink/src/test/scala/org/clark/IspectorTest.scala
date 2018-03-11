package org.clark
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.sql.Timestamp

@RunWith(classOf[JUnitRunner])
class IspectorTest extends FunSuite {
  import Inspector._
  test("check bad ips"){
    assert(!checkBadIP("1.1.1.1"))
    assert(checkBadIP("1.1.1.0"))
    assert(!checkBadIP("1.12.1.0"))
  }
}