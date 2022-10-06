package kg.optima.mobile.registration.presentation.sms_code

import kg.optima.mobile.base.presentation.BaseMppState
import kg.optima.mobile.core.common.Constants
import kg.optima.mobile.feature.registration.RegistrationScreenModel

class SmsCodeState : BaseMppState<CheckSmsCodeInfo>() {

    override fun handle(entity: CheckSmsCodeInfo) {
        val stateModel: StateModel = when (entity) {
            is CheckSmsCodeInfo.OtpCheck -> {
                if (entity.success) {
                    val screenModel = RegistrationScreenModel.SelfConfirm
                    StateModel.Navigate(screenModel)
                } else {
                    StateModel.Error.BaseError(Constants.OTP_INVALID_ERROR_CODE)
                }
            }
            is CheckSmsCodeInfo.Check ->
                if (entity.success) {
                    SmsCodeStateModel.Request
                } else {
                    StateModel.Error.BaseError("Не удалось запросить новый смс-код.")
                }
            is CheckSmsCodeInfo.TimeLeft -> SmsCodeStateModel.TimeLeft(entity.timeout)
        }
        setStateModel(stateModel)
    }

    sealed interface SmsCodeStateModel : StateModel {

        object Request : SmsCodeStateModel

        class TimeLeft(
            val timeLeft: Int
        ) : SmsCodeStateModel
    }

}
