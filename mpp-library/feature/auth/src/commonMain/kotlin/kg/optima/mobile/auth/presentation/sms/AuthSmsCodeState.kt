package kg.optima.mobile.auth.presentation.sms

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.auth.presentation.sms.model.AuthCheckSmsCodeInfo
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.common.presentation.CheckSmsCodeInfo
import kg.optima.mobile.common.presentation.SmsCodeState

class AuthSmsCodeState : SmsCodeState() {
	override fun handle(entity: CheckSmsCodeInfo) {
		when (entity) {
			AuthCheckSmsCodeInfo.Success -> setStateModel(Model.NavigateTo.PinEnter)
			else -> super.handle(entity)
		}
	}

	sealed interface Model : UiState.Model {
		sealed interface NavigateTo : Model, UiState.Model.Navigate {
			@Parcelize
			object PinEnter : NavigateTo

			@Parcelize
			object Main : NavigateTo
		}
	}
}