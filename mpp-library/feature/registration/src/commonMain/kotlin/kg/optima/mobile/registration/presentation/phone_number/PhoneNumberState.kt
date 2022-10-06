package kg.optima.mobile.registration.presentation.phone_number

import kg.optima.mobile.base.presentation.BaseMppState
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.core.common.Constants
import kg.optima.mobile.feature.registration.RegistrationScreenModel

class PhoneNumberState : BaseMppState<CheckPhoneNumberInfo>() {

    override fun handle(entity: CheckPhoneNumberInfo) {
        val stateModel: StateModel = when (entity) {
            is CheckPhoneNumberInfo.Validation ->
                PhoneNumberStateModel.ValidateResult(entity.success, entity.message)
            is CheckPhoneNumberInfo.Check -> {
                if (entity.success) {
                    val screenModel = RegistrationScreenModel.AcceptCode(
                        phoneNumber = phoneFormatter(entity.phoneNumber),
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

    private fun phoneFormatter(phone: String): String {
        val builder = StringBuilder().apply {
            append(Constants.PHONE_NUMBER_CODE)
            for (i in phone.indices) {
                if (i == 0) append("(")
                append(phone[i])

                if (i == 2) append(") ")
                if (i % 2 == 0 && i != 8 && i > 2) append(" ")
            }
        }
        return builder.toString()
    }

    sealed interface PhoneNumberStateModel : StateModel {
        class ValidateResult(
            val success: Boolean,
            val message: String = emptyString,
        ) : PhoneNumberStateModel
    }
}