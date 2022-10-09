package kg.optima.mobile.auth.domain.usecase.login

import kg.optima.mobile.feature.auth.model.AuthOtpModel

sealed interface LoginSignInResult {
	class SuccessAuth(
		val firstAuth: Boolean,
	) : LoginSignInResult

	class SmsCodeRequired(
		val otpModel: AuthOtpModel
	) : LoginSignInResult

	class IncorrectData(
		val message: String?,
	) : LoginSignInResult

	object UserBlocked : LoginSignInResult

	object Error : LoginSignInResult
}