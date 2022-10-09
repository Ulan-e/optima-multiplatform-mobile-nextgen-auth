package kg.optima.mobile.auth.presentation.pin_enter

import kg.optima.mobile.auth.presentation.login.LoginState
import kg.optima.mobile.auth.presentation.login.model.LoginModel

open class PinEnterState : LoginState() {

	override fun handle(entity: LoginModel) {
		super.handle(entity)
		//TODO logout
		when (entity) {
			is LoginModel.ClientInfo ->
				if (entity.biometryEnabled) setStateModel(LoginState.Model.Biometry)
			else -> Unit
		}
	}

	sealed interface Model : LoginState.Model {

	}
}