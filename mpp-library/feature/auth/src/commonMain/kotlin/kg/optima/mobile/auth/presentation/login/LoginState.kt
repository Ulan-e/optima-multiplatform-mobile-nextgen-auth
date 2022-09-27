package kg.optima.mobile.auth.presentation.login

import kg.optima.mobile.auth.presentation.login.model.LoginModel
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.core.navigation.ScreenModel

class LoginState(
	private val nextScreenModel: ScreenModel,
) : State<LoginModel>() {

	override fun handle(entity: LoginModel) {
		val state: StateModel = when (entity) {
			is LoginModel.SignInResult -> when (entity) {
				LoginModel.SignInResult.Error -> TODO()
				is LoginModel.SignInResult.IncorrectData -> LoginStateModel.SignInResult.IncorrectData(
					entity.message ?: "Неверный ID код или пароль"
				)
				LoginModel.SignInResult.SmsCodeRequired -> TODO()
				is LoginModel.SignInResult.SuccessAuth -> StateModel.Navigate(nextScreenModel)
				LoginModel.SignInResult.UserBlocked -> TODO()
			}
			is LoginModel.ClientId ->
				LoginStateModel.ClientId(clientId = entity.id)
			is LoginModel.Biometry ->
				if (entity.enabled) LoginStateModel.ShowBiometry else StateModel.Initial
		}

		setStateModel(state)
	}

	sealed interface LoginStateModel : StateModel {
		object ShowBiometry : LoginStateModel
		class ClientId(val clientId: String?) : LoginStateModel

		sealed interface SignInResult : LoginStateModel {
			class IncorrectData(val message: String) : SignInResult
		}
	}
}