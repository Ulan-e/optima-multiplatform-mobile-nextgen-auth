package kg.optima.mobile.auth.domain.auth_manager


interface AuthManager {
    fun isAuthorized(): Boolean
    fun isAuthorized(authorized: () -> Unit, notAuthorized: () -> Unit)
}
