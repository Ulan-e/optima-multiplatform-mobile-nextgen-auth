package kg.optima.mobile.registration.presentation.sms_code

import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.core.common.Constants
import kg.optima.mobile.feature.registration.RegistrationScreenModel

class SmsCodeState : State<CheckSmsCodeInfo>() {

    override fun handle(entity: CheckSmsCodeInfo) {
        val stateModel: StateModel = when (entity) {
            is CheckSmsCodeInfo.Check -> {
                if (entity.success) {
                    val screenModel = RegistrationScreenModel.SelfConfirm
                    StateModel.Navigate(screenModel)
                } else {
                    StateModel.Error.BaseError(Constants.OTP_INVALID_ERROR_CODE)
                }
            }
            is CheckSmsCodeInfo.ReRequest ->
                if (entity.success) {
                    SmsCodeStateModel.EnableReRequest(false)
                } else {
                    StateModel.Error.BaseError("Не удалось запросить новый смс-код.")
                }
            is CheckSmsCodeInfo.Timeout ->
                SmsCodeStateModel.Timeout(entity.timeout)
            CheckSmsCodeInfo.EnableReRequest -> SmsCodeStateModel.EnableReRequest(true)
        }
        setStateModel(stateModel)
    }

    sealed interface SmsCodeStateModel : StateModel {
        object ReRequest : SmsCodeStateModel

        class Timeout(
            val timeout: Int
        ) : SmsCodeStateModel

        class EnableReRequest(
            val enabled : Boolean
        ) : SmsCodeStateModel
    }

}
