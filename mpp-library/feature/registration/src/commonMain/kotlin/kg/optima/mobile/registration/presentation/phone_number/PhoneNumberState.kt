package kg.optima.mobile.registration.presentation.phone_number

import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.feature.registration.RegistrationScreenModel

class PhoneNumberState : State<CheckPhoneNumberInfo>() {

    override fun handle(entity: CheckPhoneNumberInfo) {
        val stateModel: StateModel = when (entity) {
            is CheckPhoneNumberInfo.Validation ->
                PhoneNumberStateModel.ValidateResult(entity.success, entity.message)
            is CheckPhoneNumberInfo.Check -> {
                if (entity.success) {
                    val screenModel = RegistrationScreenModel.AcceptCode(
                        phoneNumber = entity.phoneNumber,
                        timeLeft = entity.timeLeft,
                        referenceId = entity.referenceId
                    )
                    StateModel.Navigate(screenModel)
                } else {
                    StateModel.Error.BaseError(entity.message)
                }
            }
        }

        setStateModel(stateModel)
    }

    sealed interface PhoneNumberStateModel : StateModel {
        class ValidateResult(
            val success: Boolean,
            val message: String = emptyString,
        ) : PhoneNumberStateModel
    }
}