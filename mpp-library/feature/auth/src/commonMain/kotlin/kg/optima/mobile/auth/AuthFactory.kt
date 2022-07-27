package kg.optima.mobile.auth

import kg.optima.mobile.auth.data.api.AuthApi
import kg.optima.mobile.auth.data.api.AuthApiImpl
import kg.optima.mobile.auth.data.component.FeatureAuthComponent
import kg.optima.mobile.auth.data.component.FeatureAuthComponentImpl
import kg.optima.mobile.auth.data.repository.AuthRepository
import kg.optima.mobile.auth.data.repository.AuthRepositoryImpl
import kg.optima.mobile.auth.domain.usecase.LoginUseCase
import kg.optima.mobile.auth.presentation.login.LoginIntentHandler
import kg.optima.mobile.auth.presentation.login.LoginStateMachine
import kg.optima.mobile.base.di.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.module.Module
import org.koin.dsl.module

object AuthFactory : Factory, KoinComponent {

	override val module: Module = module {
		factory<AuthApi> { AuthApiImpl(networkClient = get()) }
		factory<AuthRepository> { AuthRepositoryImpl(authApi = get()) }
		factory<FeatureAuthComponent> {
			FeatureAuthComponentImpl(storageRepository = get(), runtimeCache = get())
		}

		//UseCases injection
		factory { LoginUseCase(authRepository = get(), component = get()) }

		// StateMachines and IntentHandlers injection by pair
		factory { LoginStateMachine() }
		factory { sm -> LoginIntentHandler(sm.get()) }
	}
}