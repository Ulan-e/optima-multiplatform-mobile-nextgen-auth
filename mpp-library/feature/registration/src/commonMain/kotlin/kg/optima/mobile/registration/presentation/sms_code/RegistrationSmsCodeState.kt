package kg.optima.mobile.registration.presentation.sms_code

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.common.presentation.CheckSmsCodeInfo
import kg.optima.mobile.common.presentation.SmsCodeState
import kg.optima.mobile.core.common.Constants

class RegistrationSmsCodeState : SmsCodeState() {

	override fun handle(entity: CheckSmsCodeInfo) {
		when (entity) {
			is RegistrationCheckSmsCodeInfo.OtpCheck -> {
				if (entity.success) {
					SmsCodeStateModel.NavigateToSelfConfirm
				} else {
					setStateModel(Model.Error.BaseError(Constants.OTP_INVALID_ERROR_CODE))
				}
			}
			is RegistrationCheckSmsCodeInfo.Check ->
				if (entity.success) {
					setStateModel(SmsCodeStateModel.Request)
				} else {
					setStateModel(
						Model.Error.BaseError("Не удалось запросить новый смс-код.")
					)
				}
			else -> super.handle(entity)
		}
	}

	sealed interface SmsCodeStateModel : Model {
		object Request : SmsCodeStateModel

		@Parcelize
		object NavigateToSelfConfirm : SmsCodeStateModel, Model.Navigate
	}
}
