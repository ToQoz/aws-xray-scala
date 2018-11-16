package net.toqoz.aws.xray.jdbc

import org.scalatest._

class DriverSpec extends FunSpec with Matchers {
  describe("Driver") {
    val driver = new Driver()

    lazy val extractSpec = Seq(
      // ?driver
      (
        "jdbc:xray:mysql://localhost/test?driver=com.mysql.cj.jdbc.Driver",
        "com.mysql.cj.jdbc.Driver",
        "jdbc:mysql://localhost/test"
      ),
      // ?driver=x&
      (
        "jdbc:xray:mysql://localhost/test?driver=com.mysql.cj.jdbc.Driver&serverTimezone=UTC",
        "com.mysql.cj.jdbc.Driver",
        "jdbc:mysql://localhost/test?serverTimezone=UTC"
      ),
      // &driver=x&
      (
        "jdbc:xray:mysql://localhost/test?maxRows=1000&driver=com.mysql.cj.jdbc.Driver&serverTimezone=UTC",
        "com.mysql.cj.jdbc.Driver",
        "jdbc:mysql://localhost/test?maxRows=1000&serverTimezone=UTC"
      ),
      // &driver=x
      (
        "jdbc:xray:mysql://localhost/test?maxRows=1000&serverTimezone=UTC&driver=com.mysql.cj.jdbc.Driver",
        "com.mysql.cj.jdbc.Driver",
        "jdbc:mysql://localhost/test?maxRows=1000&serverTimezone=UTC"
      )
    )
    describe("#extractDriverName") {
      extractSpec.foreach { t =>
        it(s"should return ${t._2} for ${t._1}") {
          driver.extractDriverName(t._1) should be(t._2)
        }
      }
    }
    describe("#extractUrl") {
      extractSpec.foreach { t =>
        it(s"should return ${t._3} for ${t._1}") {
          driver.extractUrl(t._1) should be(t._3)
        }
      }
    }
  }
}
