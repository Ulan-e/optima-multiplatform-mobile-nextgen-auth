package kg.optima.mobile.auth.presentation.login

import kg.optima.mobile.auth.domain.usecase.LoginUseCase
import kg.optima.mobile.base.presentation.StateMachine

class LoginStateMachine : StateMachine<LoginUseCase.Token, LoginStateMachine.LoginState>() {

	class LoginState : State

	override fun handle(entity: LoginUseCase.Token) {
		setState(LoginState())
	}
}