package kg.optima.mobile.auth.presentation.login

import kg.optima.mobile.auth.presentation.login.model.LoginModel
import kg.optima.mobile.base.presentation.StateMachine

class LoginStateMachine : StateMachine<LoginModel>() {

	sealed interface LoginState : State {
		object SignIn : LoginState
	}

	override fun handle(entity: LoginModel) {
		val state: LoginState = when (entity) {
			is LoginModel.LoginResponse -> LoginState.SignIn
		}

		setState(state)
	}
}