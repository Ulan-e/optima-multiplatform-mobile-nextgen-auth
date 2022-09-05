package kg.optima.mobile.auth.presentation.login

import kg.optima.mobile.auth.presentation.login.model.LoginModel
import kg.optima.mobile.base.presentation.StateMachine
import kg.optima.mobile.core.navigation.ScreenModel

class LoginStateMachine(
	private val nextScreenModel: ScreenModel,
) : StateMachine<LoginModel>() {

	sealed interface LoginState : State {
		object ShowBiometry : LoginState
		class ClientId(val clientId: String?) : LoginState
	}

	override fun handle(entity: LoginModel) {
		val state = when (entity) {
			is LoginModel.Success -> State.Navigate(listOf(nextScreenModel))
			is LoginModel.ClientId -> LoginState.ClientId(clientId = entity.id)
			is LoginModel.Biometry -> if (entity.enabled) LoginState.ShowBiometry else State.Initial
		}

		setState(state)
	}
}