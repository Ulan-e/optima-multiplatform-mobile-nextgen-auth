package kg.optima.mobile.auth

import kg.optima.mobile.auth.data.api.AuthApi
import kg.optima.mobile.auth.data.api.AuthApiImpl
import kg.optima.mobile.auth.data.component.FeatureAuthComponent
import kg.optima.mobile.auth.data.component.FeatureAuthComponentImpl
import kg.optima.mobile.auth.data.repository.AuthRepository
import kg.optima.mobile.auth.data.repository.AuthRepositoryImpl
import kg.optima.mobile.auth.domain.usecase.login.LoginUseCase
import kg.optima.mobile.auth.presentation.intent.AuthIntentHandler
import kg.optima.mobile.auth.presentation.state.AuthStateMachine
//import kg.optima.mobile.auth.domain.AuthUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

object AuthFactory {
    val module: Module = module {
        factory<AuthApi> { AuthApiImpl(networkClient = get()) }
        factory<AuthRepository> { AuthRepositoryImpl(authApi = get()) }
        factory<FeatureAuthComponent> {
            FeatureAuthComponentImpl(storageRepository = get(), runtimeCache = get())
        }
        factory { AuthStateMachine() }
        factory { AuthIntentHandler() }

        useCaseFactories
    }

    private val Module.useCaseFactories: Unit
        get() {
            factory { LoginUseCase(authRepository = get(), component = get()) }
        }
}