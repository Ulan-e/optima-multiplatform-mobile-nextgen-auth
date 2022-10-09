package kg.optima.mobile.auth.presentation.login.model

import kg.optima.mobile.base.presentation.BaseEntity
import kg.optima.mobile.feature.auth.model.AuthOtpModel

interface LoginEntity : BaseEntity {
	sealed interface SignInResult : LoginEntity {
		class SuccessAuth(
			val firstAuth: Boolean,
		) : SignInResult

		class SmsCodeRequired(
			val otpModel: AuthOtpModel
		) : SignInResult

		class IncorrectData(
			val message: String?,
		) : SignInResult

		object UserBlocked : SignInResult

		object Error : SignInResult
	}

	class ClientInfo(
		val clientId: String,
		val isAuthorized: Boolean,
		val pinEnabled: Boolean,
		val biometryEnabled: Boolean,
	) : LoginEntity

	class ClientIdInfo(
		val cardNumber: String,
		val clientId: String,
		val expiredDate: String,
	) : LoginEntity
}
