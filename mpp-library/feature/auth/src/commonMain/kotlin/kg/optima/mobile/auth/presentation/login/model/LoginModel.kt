package kg.optima.mobile.auth.presentation.login.model

sealed interface LoginModel {
	class LoginResponse(
		val accessToken: String,
		val expiresIn: Int,
		val refreshToken: String,
		val refreshTokenExpiresIn: Int,
		val sessionState: String,
	) : LoginModel
}
