package kg.optima.mobile.auth.presentation.login

import kg.optima.mobile.auth.presentation.login.model.LoginModel
import kg.optima.mobile.base.presentation.StateMachine

class LoginStateMachine : StateMachine<LoginModel>() {

	sealed interface LoginState : State {
		object ShowBiometry : LoginState
		object SignIn : LoginState
		class ClientId(val clientId: String?) : LoginState
	}

	override fun handle(entity: LoginModel) {
		val state: LoginState = when (entity) {
			is LoginModel.Success -> LoginState.SignIn
			is LoginModel.ClientId -> LoginState.ClientId(clientId = entity.id)
		}

		setState(state)
	}
}