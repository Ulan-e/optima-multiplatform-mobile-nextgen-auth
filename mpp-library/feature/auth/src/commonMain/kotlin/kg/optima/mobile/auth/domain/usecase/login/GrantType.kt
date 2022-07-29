package kg.optima.mobile.auth.domain.usecase.login

enum class GrantType(val method: String) {
	Password("password"),
	Pin("pin"),
	Biometry("biometry");
}