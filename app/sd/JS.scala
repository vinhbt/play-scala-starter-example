package sd

import io.circe.{Json, JsonObject}
import play.api.mvc.Result
import play.api.mvc.Results._
import io.circe.syntax._
import Helpers._

object JS {

  @inline implicit final class JsObjectEx(val u: JsonObject) extends AnyVal {
    @inline def OKResult: Result = Ok(u.add("status", Json.fromString("OK")).asJson)
    @inline def KOResult: Result = Ok(u.add("status", Json.fromString("KO")).asJson)
  }

  object OK {
    @inline def apply(fields: (String, Json)*): Result =
      JsonObject(fields: _*).OKResult

    @inline def result[T](result: T)(implicit w: io.circe.Encoder[T]): Result =
      OK("result" -> result.asJson)
  }

  object KO {
    @inline def apply(reason: String, status: Status = Ok): Result =
      JsonObject("status" -> Json.fromString("KO"), "reason" -> Json.fromString(reason)).KOResult

    @inline
    def reason(reason: String, fields: (String, Json)*): Result =
      JsonObject(fields: _*).add("reason", Json.fromString(reason)).KOResult
  }

}
