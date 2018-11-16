package app.controllers

import org.scalatest._
import org.scalatestplus.play.guice._
import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._

class UserControllerSpec extends FunSpec with Matchers with GuiceOneAppPerTest {
  private val databaseIsAvailable = System.getenv("MYSQL_DB_NAME") != null

  describe("UserController") {
    describe("GET /users") {
      lazy val request = FakeRequest(GET, "/users")

      it("should return users") {
        assume(databaseIsAvailable)
        val res = route(app, request).get
        status(res) should be(OK)
        contentAsJson(res).as[JsObject].keys should contain("users")
      }
    }

    describe("POST /users") {
      lazy val request = FakeRequest(POST, "/users").withJsonBody(Json.obj("name" -> "name"))

      it("should create and return the user") {
        assume(databaseIsAvailable)
        val res = route(app, request).get
        status(res) should be(OK)
        (contentAsJson(res) \ "user").as[JsObject].keys should contain("id")
      }
    }
  }
}
