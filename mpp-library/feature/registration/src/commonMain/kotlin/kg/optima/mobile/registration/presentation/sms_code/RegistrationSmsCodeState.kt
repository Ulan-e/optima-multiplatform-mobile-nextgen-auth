package kg.optima.mobile.registration.presentation.sms_code

import kg.optima.mobile.common.presentation.CheckSmsCodeInfo
import kg.optima.mobile.common.presentation.SmsCodeState
import kg.optima.mobile.core.common.Constants
import kg.optima.mobile.feature.registration.RegistrationScreenModel

class RegistrationSmsCodeState : SmsCodeState() {

	override fun handle(entity: CheckSmsCodeInfo) {
		when (entity) {
			is RegistrationCheckSmsCodeInfo.OtpCheck -> {
				if (entity.success) {
					val screenModel = RegistrationScreenModel.SelfConfirm
					setStateModel(StateModel.Navigate(screenModel))
				} else {
					setStateModel(StateModel.Error.BaseError(Constants.OTP_INVALID_ERROR_CODE))
				}
			}
			is RegistrationCheckSmsCodeInfo.Check ->
				if (entity.success) {
					setStateModel(SmsCodeStateModel.Request)
				} else {
					setStateModel(
						StateModel.Error.BaseError("Не удалось запросить новый смс-код.")
					)
				}
			else -> super.handle(entity)
		}
	}

	sealed interface SmsCodeStateModel : StateModel {
		object Request : SmsCodeStateModel
	}
}
