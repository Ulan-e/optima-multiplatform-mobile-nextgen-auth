package kg.optima.mobile.auth.presentation.intent

sealed interface AuthIntent {
    class Login(
        val mobile: String,
        val password: String,
    ) : AuthIntent
}