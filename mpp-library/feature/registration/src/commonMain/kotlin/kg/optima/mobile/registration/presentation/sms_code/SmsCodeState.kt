package kg.optima.mobile.registration.presentation.sms_code

import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.feature.register.RegistrationScreenModel

class SmsCodeState : State<CheckSmsCodeInfo>() {

    override fun handle(entity: CheckSmsCodeInfo) {
        val stateModel: StateModel = when (entity) {
            is CheckSmsCodeInfo.Check -> {
                if (entity.success) {
                    val screenModel = RegistrationScreenModel.IdConfirmation
                    StateModel.Navigate(screenModel)
                } else {
                    StateModel.Error.BaseError("Неверный смс-код!")
                }
            }
            is CheckSmsCodeInfo.ReRequest ->
                SmsCodeStateModel.ReRequestSmsCode(timeout = entity.timeout)
        }
        setStateModel(stateModel)
    }

    sealed interface SmsCodeStateModel : StateModel {
        class ReRequestSmsCode(
            val timeout: Int
        ) : SmsCodeStateModel

        object InvalidSmsCode : SmsCodeStateModel
    }

}
