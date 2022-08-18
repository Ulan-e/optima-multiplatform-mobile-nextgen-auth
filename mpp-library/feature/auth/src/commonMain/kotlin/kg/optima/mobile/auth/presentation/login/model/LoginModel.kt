package kg.optima.mobile.auth.presentation.login.model

sealed interface LoginModel {
	object Success : LoginModel
	class ClientId(val id: String?) : LoginModel
}
