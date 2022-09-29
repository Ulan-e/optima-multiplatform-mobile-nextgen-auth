package kg.optima.mobile.common.presentation

import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.core.common.Constants
import kg.optima.mobile.feature.registration.RegistrationScreenModel

open class SmsCodeState : State<CheckSmsCodeInfo>() {

    override fun handle(entity: CheckSmsCodeInfo) {
        when (entity) {
            is CheckSmsCodeInfo.TimeLeft ->
                setStateModel(SmsCodeStateModel.TimeLeft(entity.timeout))
            else -> Unit
        }
    }

    sealed interface SmsCodeStateModel : StateModel {
        class TimeLeft(val timeLeft: Int) : SmsCodeStateModel
    }

}
