package kg.optima.mobile.common.presentation.launch

import kg.optima.mobile.base.di.IntentFactory
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf


object LaunchIntentFactory : IntentFactory<LaunchIntentHandler, LaunchStateMachine> {
	override val stateMachine: LaunchStateMachine by inject()
	override val intentHandler: LaunchIntentHandler by inject { parametersOf(stateMachine) }
}