package kg.optima.mobile.auth

import kg.optima.mobile.auth.data.api.AuthApi
import kg.optima.mobile.auth.data.api.AuthApiImpl
import kg.optima.mobile.auth.data.component.AuthPreferences
import kg.optima.mobile.auth.data.component.AuthPreferencesImpl
import kg.optima.mobile.auth.data.repository.AuthRepository
import kg.optima.mobile.auth.data.repository.AuthRepositoryImpl
import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfoUseCase
import kg.optima.mobile.auth.domain.usecase.login.LoginUseCase
import kg.optima.mobile.auth.domain.usecase.pin_set.PinSetUseCase
import kg.optima.mobile.auth.presentation.auth_state.AuthStateIntentHandler
import kg.optima.mobile.auth.presentation.auth_state.AuthStatusStateMachine
import kg.optima.mobile.auth.presentation.login.LoginIntentHandler
import kg.optima.mobile.auth.presentation.login.LoginStateMachine
import kg.optima.mobile.auth.presentation.pin_set.PinSetIntentHandler
import kg.optima.mobile.auth.presentation.pin_set.PinSetStateMachine
import kg.optima.mobile.base.di.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.module.Module
import org.koin.dsl.module

object AuthFeatureFactory : Factory, KoinComponent {

	override val module: Module = module {
		factory<AuthApi> { AuthApiImpl(networkClient = get()) }
		factory<AuthRepository> { AuthRepositoryImpl(authApi = get()) }
		factory<AuthPreferences> {
			AuthPreferencesImpl(storageRepository = get(), runtimeCache = get())
		}

		//UseCases injection
		factory { LoginUseCase(authRepository = get(), authPreferences = get()) }
		factory { ClientInfoUseCase(authPreferences = get()) }
		factory { PinSetUseCase(authPreferences = get()) }

		// StateMachines and IntentHandlers injection by pair
		factory { LoginStateMachine() }
		factory { sm -> LoginIntentHandler(sm.get()) }

		factory { AuthStatusStateMachine() }
		factory { sm -> AuthStateIntentHandler(sm.get()) }

		factory { PinSetStateMachine() }
		factory { sm -> PinSetIntentHandler(sm.get()) }

	}
}