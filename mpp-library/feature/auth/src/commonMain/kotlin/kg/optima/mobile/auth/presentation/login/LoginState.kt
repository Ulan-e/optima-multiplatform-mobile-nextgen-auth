package kg.optima.mobile.auth.presentation.login

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.auth.presentation.AuthNavigateModel
import kg.optima.mobile.auth.presentation.login.model.LoginEntity
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.feature.auth.model.AuthOtpModel

open class LoginState<T : LoginEntity> : UiState<T>() {

	override fun handle(entity: T) {
		val state: UiState.Model = when (entity) {
			is LoginEntity.SignInResult -> when (entity) {
				LoginEntity.SignInResult.Error -> TODO()
				is LoginEntity.SignInResult.IncorrectData ->
					Model.SignInResult.IncorrectData(entity.message ?: "Неверный ID код или пароль")
				is LoginEntity.SignInResult.SmsCodeRequired ->
					Model.NavigateTo.SmsCode(entity.otpModel)
				is LoginEntity.SignInResult.SuccessAuth ->
					if (entity.firstAuth) Model.NavigateTo.PinSet else Model.NavigateTo.MainPage
				LoginEntity.SignInResult.UserBlocked -> TODO()
			}
			is LoginEntity.ClientInfo -> {
				if (entity.isAuthorized && entity.pinEnabled) {
					Model.NavigateTo.PinEnter(entity.clientId)
				} else {
					Model.ClientId(clientId = entity.clientId)
				}
			}
			is LoginEntity.ClientIdInfo ->
				Model.NavigateTo.ClientIdInfo(
					cardNumber = entity.cardNumber,
					clientId = entity.clientId,
					expiredDate = entity.expiredDate
				)
			else -> UiState.Model.Initial
		}

		setStateModel(state)
	}

	interface Model : UiState.Model {
		class ClientId(
			val clientId: String,
		) : Model

		sealed interface NavigateTo : Model, AuthNavigateModel {
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