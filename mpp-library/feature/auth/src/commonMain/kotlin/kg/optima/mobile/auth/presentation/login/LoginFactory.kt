package kg.optima.mobile.auth.presentation.login

import kg.optima.mobile.base.di.IntentFactory
import kg.optima.mobile.core.navigation.ScreenModel
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf


class LoginFactory(
	private val nextScreenModel: ScreenModel
) : IntentFactory<LoginIntentHandler, LoginStateMachine> {
	override val stateMachine: LoginStateMachine by inject { parametersOf(nextScreenModel) }
	override val intentHandler: LoginIntentHandler by inject { parametersOf(stateMachine) }
}