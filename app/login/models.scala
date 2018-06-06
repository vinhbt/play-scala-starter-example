package login

case class AuthModel(userId: Int, provider: String, providerKey: String, accessToken: String)