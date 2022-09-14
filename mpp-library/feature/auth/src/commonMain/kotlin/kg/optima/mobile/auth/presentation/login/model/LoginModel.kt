package kg.optima.mobile.auth.presentation.login.model

sealed interface LoginModel {
	class Success(val firstAuth: Boolean) : LoginModel
	class ClientId(val id: String?) : LoginModel
	class Biometry(val enabled: Boolean) : LoginModel
}
