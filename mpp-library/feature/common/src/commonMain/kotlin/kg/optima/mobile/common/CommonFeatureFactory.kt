package kg.optima.mobile.common

import kg.optima.mobile.base.di.Factory
import kg.optima.mobile.common.domain.LaunchUseCase
import kg.optima.mobile.common.presentation.welcome.WelcomeIntent
import kg.optima.mobile.common.presentation.welcome.WelcomeState
import org.koin.core.module.Module
import org.koin.dsl.module

object CommonFeatureFactory : Factory {

	override val module: Module = module {

		factory { LaunchUseCase(get()) }

		// States and Intents injection by pair
		factory { WelcomeState() }
		factory { sm -> WelcomeIntent(sm.get(), get()) }

	}
}