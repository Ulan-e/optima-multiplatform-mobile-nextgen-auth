package kg.optima.mobile.registration.presentation.sms_code

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.common.presentation.sms.CheckSmsCodeInfo
import kg.optima.mobile.common.presentation.sms.SmsCodeState
import kg.optima.mobile.core.common.Constants
import kg.optima.mobile.registration.presentation.RegistrationNavigateModel

class RegistrationSmsCodeState : SmsCodeState() {

	override fun handle(entity: CheckSmsCodeInfo) {
		when (entity) {
			is RegistrationCheckSmsCodeInfo -> {
				val stateModel = when (entity) {
					is RegistrationCheckSmsCodeInfo.OtpCheck -> {
						if (entity.success) {
							Model.NavigateTo.SelfConfirm
						} else {
							UiState.Model.Error.BaseError(Constants.OTP_INVALID_ERROR_CODE)
						}
					}
					is RegistrationCheckSmsCodeInfo.Check ->
						if (entity.success) {
							Model.Request
						} else {
							UiState.Model.Error.BaseError("Не удалось запросить новый смс-код.")
						}
				}
				setStateModel(stateModel)
			}
			else -> super.handle(entity)
		}
	}

	sealed interface Model : UiState.Model {
		object Request : Model

		sealed interface NavigateTo : Model, RegistrationNavigateModel {
			@Parcelize
			object SelfConfirm : NavigateTo, RegistrationNavigateModel
		}
	}
}
