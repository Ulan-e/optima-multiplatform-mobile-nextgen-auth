package kg.optima.mobile.auth.presentation.login

import kg.optima.mobile.auth.data.api.model.login.LoginResponse
import kg.optima.mobile.base.presentation.StateMachine

class LoginStateMachine : StateMachine<LoginResponse>() {

	sealed interface LoginState : State {
		class SignIn : LoginState
	}

	override fun handle(entity: LoginResponse) {
		val state = LoginState.SignIn()

		setState(state)
	}
}