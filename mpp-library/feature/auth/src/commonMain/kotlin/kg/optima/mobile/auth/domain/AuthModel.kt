package kg.optima.mobile.auth.domain

sealed interface AuthModel {
    class Token(
        val jwt: String,
        val refreshToken: String = "",
        val refreshTokenExp: String = ""
    ) : AuthModel
}