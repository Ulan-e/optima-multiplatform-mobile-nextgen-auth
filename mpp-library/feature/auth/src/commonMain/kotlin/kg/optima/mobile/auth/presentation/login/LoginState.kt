package kg.optima.mobile.auth.presentation.login

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.auth.presentation.login.model.LoginModel
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.feature.auth.model.AuthOtpModel

open class LoginState : UiState<LoginModel>() {

	override fun handle(entity: LoginModel) {
		val state: UiState.Model = when (entity) {
			is LoginModel.SignInResult -> when (entity) {
				LoginModel.SignInResult.Error -> TODO()
				is LoginModel.SignInResult.IncorrectData ->
					Model.SignInResult.IncorrectData(entity.message ?: "Неверный ID код или пароль")
				is LoginModel.SignInResult.SmsCodeRequired ->
					Model.NavigateTo.SmsCode(entity.otpModel)
				is LoginModel.SignInResult.SuccessAuth ->
					if (entity.firstAuth) Model.NavigateTo.PinSet else Model.NavigateTo.MainPage
				LoginModel.SignInResult.UserBlocked -> TODO()
			}
			is LoginModel.ClientInfo -> {
				if (entity.isAuthorized && entity.pinEnabled) {
					Model.NavigateTo.PinEnter(entity.clientId)
				} else {
					Model.ClientId(clientId = entity.clientId)
				}
			}
			is LoginModel.ClientIdInfo ->
				Model.NavigateTo.ClientIdInfo(
					cardNumber = entity.cardNumber,
					clientId = entity.clientId,
					expiredDate = entity.expiredDate
				)
		}

		setStateModel(state)
	}

	interface Model : UiState.Model {
		class ClientId(
			val clientId: String,
		) : Model

		sealed interface NavigateTo : Model, UiState.Model.Navigate {
			@Parcelize
			class ClientIdInfo(
				val cardNumber: String,
				val clientId: String,
				val expiredDate: String,
			) : NavigateTo

			@Parcelize
			class PinEnter(
				val clientId: String,
			) : NavigateTo

			@Parcelize
			class SmsCode(
				val otpModel: AuthOtpModel
			) : NavigateTo

			@Parcelize
			object PinSet : NavigateTo

			@Parcelize
			object MainPage : NavigateTo
		}

		object Biometry : Model

		sealed interface SignInResult : Model {
			class IncorrectData(val message: String) : SignInResult
		}
	}
}