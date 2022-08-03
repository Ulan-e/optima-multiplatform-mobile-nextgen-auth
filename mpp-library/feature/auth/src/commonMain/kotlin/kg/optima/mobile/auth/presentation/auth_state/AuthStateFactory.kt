package kg.optima.mobile.auth.presentation.auth_state

import kg.optima.mobile.base.di.IntentFactory
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf


object AuthStateFactory : IntentFactory<AuthStateIntentHandler, AuthStatusStateMachine> {
	override val stateMachine: AuthStatusStateMachine by inject()
	override val intentHandler: AuthStateIntentHandler by inject { parametersOf(stateMachine) }
}