package org.example.library.feature.auth.model

interface ServerApi {
    suspend fun login(login: String, password: String)
}
