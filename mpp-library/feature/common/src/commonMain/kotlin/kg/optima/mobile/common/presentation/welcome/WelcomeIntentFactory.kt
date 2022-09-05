package kg.optima.mobile.common.presentation.welcome

import kg.optima.mobile.base.di.IntentFactory
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf


object WelcomeIntentFactory : IntentFactory<WelcomeIntentHandler, WelcomeStateMachine> {
	override val stateMachine: WelcomeStateMachine by inject()
	override val intentHandler: WelcomeIntentHandler by inject { parametersOf(stateMachine) }
}