package kg.optima.mobile

import dev.icerock.moko.errors.handler.ExceptionHandler
import dev.icerock.moko.network.generated.apis.AuthApi
import dev.icerock.moko.network.generated.models.SignInRequest
import kg.optima.mobile.feature.auth.di.AuthFactory
import kg.optima.mobile.feature.auth.model.ServerApi

internal fun AuthFactory(
    createExceptionHandler: () -> ExceptionHandler,
    authApi: AuthApi
) = AuthFactory(
    createExceptionHandler = createExceptionHandler,
    serverApi = object : ServerApi {
        override suspend fun login(login: String, password: String) {
            authApi.signIn(SignInRequest(email = login, password = password))
        }
    }
)
