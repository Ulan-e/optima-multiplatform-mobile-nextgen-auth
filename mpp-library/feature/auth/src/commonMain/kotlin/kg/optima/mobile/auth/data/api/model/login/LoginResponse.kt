package kg.optima.mobile.auth.data.api.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LoginResponse(
	@SerialName("access_token")
	val accessToken: String,

	@SerialName("expires_in")
	val expiresIn: Int,

	@SerialName("refresh_token")
	val refreshToken: String,

	@SerialName("refresh_expires_in")
	val refreshTokenExpiresIn: Int,

	@SerialName("session_state")
	val sessionState: String,
)