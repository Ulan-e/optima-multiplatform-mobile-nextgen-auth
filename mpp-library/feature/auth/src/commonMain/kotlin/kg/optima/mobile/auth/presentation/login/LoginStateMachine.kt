package kg.optima.mobile.auth.presentation.login

import kg.optima.mobile.auth.data.api.model.login.UserAuthenticationResponse
import kg.optima.mobile.base.presentation.StateMachine

class LoginStateMachine : StateMachine<UserAuthenticationResponse>() {

	class LoginState : State

	override fun handle(entity: UserAuthenticationResponse) {
		setState(LoginState())
	}
}