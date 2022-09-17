package kg.optima.mobile.registration.presentation.sms_code

import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.feature.registration.RegistrationScreenModel

class SmsCodeState : State<CheckSmsCodeInfo>() {

    override fun handle(entity: CheckSmsCodeInfo) {
        val stateModel: StateModel = when (entity) {
            is CheckSmsCodeInfo.Check -> {
                if (entity.success) {
                    val screenModel = RegistrationScreenModel.SelfConfirm
                    StateModel.Navigate(screenModel)
                } else {
                    SmsCodeStateModel.InvalidCodeError()
//                    StateModel.Error.BaseError(entity.message)
                }
            }
            is CheckSmsCodeInfo.ReRequest ->
                if (entity.success) {
                    SmsCodeStateModel.EnableReRequest(false)
                } else {
                    SmsCodeStateModel.InvalidCodeError()
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

        class InvalidCodeError(
            val error : String = "Неверный Код."
        ) : SmsCodeStateModel
    }

}
