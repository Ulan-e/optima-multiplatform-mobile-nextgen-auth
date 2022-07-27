package kg.optima.mobile.auth.presentation.login

import kg.optima.mobile.base.di.IntentFactory
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf


object LoginFactory : IntentFactory<LoginIntentHandler, LoginStateMachine> {
	override val stateMachine: LoginStateMachine by inject()
	override val intentHandler: LoginIntentHandler by inject { parametersOf(stateMachine) }
}