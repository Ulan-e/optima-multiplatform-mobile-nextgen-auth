package kg.optima.mobile.auth

import kg.optima.mobile.auth.data.api.AuthApi
import kg.optima.mobile.auth.data.api.AuthApiImpl
import kg.optima.mobile.auth.domain.AuthUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

object AuthFactory {
    val module: Module = module {
        factory<AuthApi> { AuthApiImpl(networkClient = get()) }
        factory { AuthUseCase(authApi = get()) }
    }
}