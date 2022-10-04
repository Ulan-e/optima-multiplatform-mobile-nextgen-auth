package kg.optima.mobile.auth

import kg.optima.mobile.auth.data.api.AuthApi
import kg.optima.mobile.auth.data.api.AuthApiImpl
import kg.optima.mobile.auth.data.component.AuthPreferences
import kg.optima.mobile.auth.data.component.AuthPreferencesImpl
import kg.optima.mobile.auth.data.repository.AuthRepository
import kg.optima.mobile.auth.data.repository.AuthRepositoryImpl
import kg.optima.mobile.auth.domain.usecase.biometry_auth.SetupBiometryUseCase
import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfoUseCase
import kg.optima.mobile.auth.domain.usecase.login.LoginUseCase
import kg.optima.mobile.auth.domain.usecase.pin_set.PinSetUseCase
import kg.optima.mobile.auth.presentation.login.LoginIntent
import kg.optima.mobile.auth.presentation.login.LoginState
import kg.optima.mobile.auth.presentation.setup_auth.SetupAuthIntent
import kg.optima.mobile.auth.presentation.setup_auth.SetupAuthState
import kg.optima.mobile.base.di.Factory
import org.koin.core.module.Module
import org.koin.dsl.module

object AuthFeatureFactory : Factory {

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
		factory { SetupBiometryUseCase(authPreferences = get()) }

		// States and Intents injection by pair
		factory { next -> LoginState(next.get()) }
		factory { st -> LoginIntent(st.get()) }

		factory { next -> SetupAuthState(next.get()) }
		factory { st -> SetupAuthIntent(st.get()) }
	}
}