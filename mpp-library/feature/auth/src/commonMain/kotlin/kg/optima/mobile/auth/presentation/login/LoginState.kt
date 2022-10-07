package kg.optima.mobile.auth.presentation.login

import kg.optima.mobile.auth.presentation.login.model.LoginModel
import kg.optima.mobile.base.presentation.BaseMppState
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.feature.auth.AuthScreenModel

class LoginState(
	private val nextScreenModel: ScreenModel,
) : BaseMppState<LoginModel>() {

	override fun handle(entity: LoginModel) {
		val state: StateModel = when (entity) {
			is LoginModel.SignInResult -> when (entity) {
				LoginModel.SignInResult.Error -> TODO()
				is LoginModel.SignInResult.IncorrectData ->
					LoginStateModel.SignInResult.IncorrectData(
						message = entity.message ?: "Неверный ID код или пароль"
					)
				is LoginModel.SignInResult.SmsCodeRequired ->
					StateModel.Navigate(AuthScreenModel.SmsCode(nextScreenModel, entity.otpModel))
				is LoginModel.SignInResult.SuccessAuth -> LoginStateModel.Hell
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
		object Hell: LoginStateModel

		sealed interface SignInResult : LoginStateModel {
			class IncorrectData(val message: String) : SignInResult
		}
	}
}