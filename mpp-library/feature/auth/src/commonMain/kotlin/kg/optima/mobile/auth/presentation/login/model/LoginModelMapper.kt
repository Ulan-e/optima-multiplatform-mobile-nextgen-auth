package kg.optima.mobile.auth.presentation.login.model

import kg.optima.mobile.auth.data.api.model.login.LoginResponse

fun LoginResponse.toPlainModel() = LoginModel.LoginResponse(
	accessToken = accessToken,
	expiresIn = expiresIn,
	refreshToken = refreshToken,
	refreshTokenExpiresIn = refreshTokenExpiresIn,
	sessionState = sessionState
)