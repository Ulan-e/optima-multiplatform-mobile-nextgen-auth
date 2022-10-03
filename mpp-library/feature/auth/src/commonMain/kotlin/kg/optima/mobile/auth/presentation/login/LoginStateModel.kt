package kg.optima.mobile.auth.presentation.login

import kg.optima.mobile.auth.presentation.login.model.LoginModel
import kg.optima.mobile.base.presentation.BaseMppState
import kg.optima.mobile.core.navigation.ScreenModel

class LoginState(
	private val nextScreenModel: ScreenModel,
) : BaseMppState<LoginModel>() {

	sealed interface LoginStateModel : StateModel {
		object ShowBiometry : LoginStateModel
		class ClientId(val clientId: String?) : LoginStateModel
	}

	override fun handle(entity: LoginModel) {
		val state = when (entity) {
			is LoginModel.Success ->
				StateModel.Navigate(nextScreenModel)
			is LoginModel.ClientId ->
				LoginStateModel.ClientId(clientId = entity.id)
			is LoginModel.Biometry ->
				if (entity.enabled) LoginStateModel.ShowBiometry else StateModel.Initial
		}

		setStateModel(state)
	}
}