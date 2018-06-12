package login

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._


case class AuthModel(userId: Int, provider: String, providerKey: String, accessToken: String)

object AuthModel {
  implicit val authDecoder: Decoder[AuthModel] = deriveDecoder
  implicit val authEncoder: Encoder[AuthModel] = deriveEncoder
}

case class LoginReq(uid: String, provider: String)
object LoginReq {
  implicit val loginReqDecoder: Decoder[LoginReq] = deriveDecoder
  implicit val loginReqEncoder: Encoder[LoginReq] = deriveEncoder
}