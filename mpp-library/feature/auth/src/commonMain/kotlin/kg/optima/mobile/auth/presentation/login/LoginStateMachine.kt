package kg.optima.mobile.auth.presentation.login

import kg.optima.mobile.auth.presentation.login.model.LoginModel
import kg.optima.mobile.base.presentation.StateMachine
import kg.optima.mobile.feature.auth.AuthScreenModel
import kg.optima.mobile.feature.main.MainScreenModel

class LoginStateMachine : StateMachine<LoginModel>() {

	sealed interface LoginState : State {
		object ShowBiometry : LoginState
		class ClientId(val clientId: String?) : LoginState
	}

	override fun handle(entity: LoginModel) {
		val state = when (entity) {
			is LoginModel.Success -> {
				val screenModel =
					if (entity.firstAuth) AuthScreenModel.PinSet else MainScreenModel.Main
				State.Navigate(listOf(screenModel))
			}
			is LoginModel.ClientId -> LoginState.ClientId(clientId = entity.id)
			is LoginModel.Biometry -> if (entity.show) LoginState.ShowBiometry else State.Initial
		}

		setState(state)
	}
}