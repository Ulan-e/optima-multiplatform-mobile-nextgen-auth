package kg.optima.mobile.feature.auth.model

interface ServerApi {
    suspend fun login(login: String, password: String)
}
