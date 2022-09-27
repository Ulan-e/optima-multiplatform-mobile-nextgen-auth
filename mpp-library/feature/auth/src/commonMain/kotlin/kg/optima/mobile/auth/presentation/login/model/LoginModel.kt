package kg.optima.mobile.auth.presentation.login.model

sealed interface LoginModel {
	sealed interface SignInResult : LoginModel {
		class SuccessAuth(
			val firstAuth: Boolean,
			val bankId: String,
			val accessToken: String,
		) : SignInResult

		object SmsCodeRequired : SignInResult

		class IncorrectData(
			val message: String?,
		) : SignInResult

		object UserBlocked : SignInResult

		object Error : SignInResult
	}

	class ClientId(val id: String?) : LoginModel
	class Biometry(val enabled: Boolean) : LoginModel
}
