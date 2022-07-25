package kg.optima.mobile.auth.presentation.state

sealed interface AuthState {
    object Login : AuthState
}