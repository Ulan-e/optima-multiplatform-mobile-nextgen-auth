package kg.optima.mobile.auth.presentation.setup_auth

import kg.optima.mobile.base.di.IntentFactory
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf


object SetupAuthFactory : IntentFactory<SetupAuthIntentHandler, SetupAuthStateMachine> {
	override val stateMachine: SetupAuthStateMachine by inject()
	override val intentHandler: SetupAuthIntentHandler by inject { parametersOf(stateMachine) }
}