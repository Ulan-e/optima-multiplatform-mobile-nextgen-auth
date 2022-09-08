package kg.optima.mobile.common

import kg.optima.mobile.base.di.Factory
import kg.optima.mobile.common.presentation.launch.LaunchIntent
import kg.optima.mobile.common.presentation.launch.LaunchStateMachine
import kg.optima.mobile.common.presentation.welcome.WelcomeIntent
import kg.optima.mobile.common.presentation.welcome.WelcomeStateMachine
import org.koin.core.module.Module
import org.koin.dsl.module

object CommonFeatureFactory : Factory() {

	override val module: Module = module {

		// StateMachines and IntentHandlers injection by pair
		factory { LaunchStateMachine() }
		factory { sm -> LaunchIntent(sm.get()) }

		factory { WelcomeStateMachine() }
		factory { sm -> WelcomeIntent(sm.get()) }

	}
}