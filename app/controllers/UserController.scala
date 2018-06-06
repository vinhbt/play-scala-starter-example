package controllers

import javax.inject._
import play.api.db.Database
import play.api.libs.circe.Circe
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class UserController @Inject()(cc: ControllerComponents,
                               db: Database)(implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) with Circe {


  def login = Action.async(circe.json) {
    ???
  }

}
