package kg.optima.mobile.auth.presentation.login

import kg.optima.mobile.auth.domain.usecase.LoginUseCase
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.presentation.IntentHandler
import kotlinx.coroutines.delay
import org.koin.core.component.inject

class LoginIntentHandler(
	override val stateMachine: LoginStateMachine,
) : IntentHandler<LoginIntentHandler.LoginIntent, LoginUseCase.Token, LoginStateMachine.LoginState>() {

	class LoginIntent(
		val mobile: String,
		val password: String,
	) : Intent

	private val loginUseCase: LoginUseCase by inject()

	override fun dispatch(intent: LoginIntent) {
		launchOperation<LoginUseCase.Token> {
			val model = LoginUseCase.Params(
				mobile = intent.mobile,
				password = intent.password,
			)
			delay(300)

			Either.Right(LoginUseCase.Token(""))

//        return loginUseCase.execute(model)
		}
	}
}
