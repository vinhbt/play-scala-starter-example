package login

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._


case class AuthModel(userId: Int, provider: String, providerKey: String, accessToken: String)

object AuthModel {
  implicit val fooDecoder: Decoder[AuthModel] = deriveDecoder
  implicit val fooEncoder: Encoder[AuthModel] = deriveEncoder
}