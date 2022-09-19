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
            is CheckSmsCodeInfo.TimeLeft ->
                SmsCodeStateModel.TimeLeft(entity.timeout)
            CheckSmsCodeInfo.EnableReRequest -> SmsCodeStateModel.EnableReRequest(true)
            is CheckSmsCodeInfo.TriesData -> SmsCodeStateModel.TriesData(entity.tryCount, entity.timeLeft)
            CheckSmsCodeInfo.TryDataSaved -> TODO()
        }
        setStateModel(stateModel)
    }

    sealed interface SmsCodeStateModel : StateModel {

        object ReRequest : SmsCodeStateModel

        object TryDataSaved : SmsCodeStateModel

        class TimeLeft(
            val timeLeft: Int
        ) : SmsCodeStateModel

        class TriesData(
            val tryCount: Int,
            val timeLeft: Int
        ) : SmsCodeStateModel

        class EnableReRequest(
            val enabled : Boolean
        ) : SmsCodeStateModel
    }

}
