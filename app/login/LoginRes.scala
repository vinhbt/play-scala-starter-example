package login

import javax.inject.{Inject, Singleton}
import doobie.implicits._
import io.circe.Json
import play.api.db.Database
import sd.Helpers.Transaction
import sd.JS

import scala.concurrent.Future
import scala.language.implicitConversions
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class LoginRes @Inject() (db: Database){

  def all: List[AuthModel] = db.withTrans {
    sql"""select id, provider, provider_key, access_token from auth""".query[AuthModel].to[List]
  }.unsafeRunSync()

  def insert(req: LoginReq) = {
    val authToken = Random.alphanumeric.take(20).mkString
    db.withTrans {
      sql"""INSERT INTO auth(provider, provider_key, access_token) values (${req.provider}, ${req.uid}, $authToken)""".update.run
    }.unsafeToFuture().map(_ => authToken)
  }

  def check(req: LoginReq): Future[Option[AuthModel]] = db.withTrans {
    sql"""SELECT id, provider, provider_key, access_token FROM auth WHERE provider_key=${req.uid} AND provider=${req.provider}""".query[AuthModel].option
  }.unsafeToFuture()

  def login(req: LoginReq) = {
    check(req).flatMap {
        case None => insert(req).map(x => JS.OK("accessToken" -> Json.fromString(x)))
        case Some(a) => Future successful JS.OK("accessToken" -> Json.fromString(a.accessToken))
    }
  }
}
