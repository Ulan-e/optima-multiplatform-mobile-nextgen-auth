package kg.optima.mobile.feature.auth.model

enum class GrantType(val method: String) {
	Password("password"),
	Pin("pin"),
	Biometry("biometry");
}