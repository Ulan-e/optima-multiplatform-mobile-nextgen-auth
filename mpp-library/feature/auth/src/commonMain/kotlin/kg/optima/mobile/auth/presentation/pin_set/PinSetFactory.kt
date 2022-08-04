package kg.optima.mobile.auth.presentation.pin_set

import kg.optima.mobile.base.di.IntentFactory
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf


object PinSetFactory : IntentFactory<PinSetIntentHandler, PinSetStateMachine> {
	override val stateMachine: PinSetStateMachine by inject()
	override val intentHandler: PinSetIntentHandler by inject { parametersOf(stateMachine) }
}