package kg.optima.mobile.auth

import io.ktor.http.*
import kg.optima.mobile.auth.data.api.AuthApi
import kg.optima.mobile.auth.data.api.AuthApiImpl
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
import kg.optima.mobile.auth.presentation.sms.AuthSmsCodeIntent
import kg.optima.mobile.auth.presentation.sms.AuthSmsCodeState
import kg.optima.mobile.base.di.Factory
import kg.optima.mobile.network.di.provideNetworkClient
import kg.optima.mobile.network.failure.NetworkFailureImpl
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

object AuthFeatureFactory : Factory() {

	override val module: Module = module {
		factory(qualifier = named("AuthHttpClient")) {
			provideAuthHttpClient(
				kotlinxSerializer = get(),
				networkFailure = NetworkFailureImpl(json = get()),
				params = mapOf(
					HttpHeaders.UserAgent to "Optima24/2.10.3 (Android/12; OnePlus 8/in2010asdas)"
				),
			)
		}
		factory(qualifier = named("AuthNetworkClient")) {
			provideNetworkClient(
				httpClient = get(qualifier = named("AuthHttpClient")),
				json = get(),
			)
		}
		factory<AuthApi> { AuthApiImpl(networkClient = get(named("AuthNetworkClient"))) }
		factory<AuthRepository> { AuthRepositoryImpl(authApi = get()) }

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

		factory { next -> AuthSmsCodeState(next.get()) }
		factory { st -> AuthSmsCodeIntent(st.get()) }
	}
}