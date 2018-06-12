package controllers

import javax.inject._
import play.api.db.Database
import play.api.libs.circe.Circe
import io.circe.syntax._
import login.{LoginReq, LoginRes}
import play.api.Logger
import play.api.mvc._
import login.LoginReq.loginReqDecoder
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class UserController @Inject()(cc: ControllerComponents,
                               loginRes: LoginRes,
                               db: Database)(implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) with Circe {


  def login = Action.async(circe.json[LoginReq]) { req =>
    loginRes.check(req.body).foreach( u =>
      Logger.info(u.asJson.toString())
    )
    loginRes.login(req.body)
  }

}
