package org.example.library

import dev.icerock.moko.errors.handler.ExceptionHandler
//import dev.icerock.moko.network.generated.apis.AuthApi
//import dev.icerock.moko.network.generated.models.SignInRequest
import org.example.library.feature.auth.di.AuthFactory
import org.example.library.feature.auth.model.ServerApi

internal fun AuthFactory(
    createExceptionHandler: () -> ExceptionHandler,
//    authApi: AuthApi
) = AuthFactory(
    createExceptionHandler = createExceptionHandler,
    serverApi = object : ServerApi {
        override suspend fun login(login: String, password: String) {
//            authApi.signIn(SignInRequest(email = login, password = password))
        }
    }
)
