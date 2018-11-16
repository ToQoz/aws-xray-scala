package app.controllers

import scala.concurrent.{ ExecutionContext, Future }
import com.google.inject.Inject
import play.api.libs.json.Json
import play.api._
import play.api.libs.json._
import play.api.mvc._
import scalikejdbc._
import app.models._
import net.toqoz.aws.xray.play.AWSXRayPlayContext

class UserController @Inject()(cc: ControllerComponents) extends InjectedController {
  implicit val userWrites: Writes[User] = Json.writes[User]

  def create(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    AWSXRayPlayContext.withTraceEntity(cc.executionContext) { implicit ec: ExecutionContext =>
      Logger.info(s"create() ${Thread.currentThread().getId}")
      request.body.asJson
        .flatMap(json => (json \ "name").asOpt[String])
        .map { name =>
          NamedDB('writer) localTx { implicit session =>
            val id = User.create(name)
            Ok(Json.obj("user" -> User.find(id)))
          }
        }
        .getOrElse {
          BadRequest(Json.obj("error" -> "Missing parameter name"))
        }
    }
  }

  def list(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    AWSXRayPlayContext.withTraceEntity(cc.executionContext) { implicit ec: ExecutionContext =>
      Logger.info(s"list() ${Thread.currentThread().getId}")
      NamedDB('default) localTx { implicit session =>
        Ok(Json.obj("users" -> User.findAll()))
      }
    }
  }

  def listAsync(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    AWSXRayPlayContext.withTraceEntity(cc.executionContext) { implicit ec: ExecutionContext =>
      Logger.info(s"listAsync() ${Thread.currentThread().getId}")
      Future {
        Logger.info(s"listAsync()/Future ${Thread.currentThread().getId}")
        NamedDB('default) localTx { implicit session =>
          Ok(Json.obj("users" -> User.findAll()))
        }
      }
    }
  }
}
