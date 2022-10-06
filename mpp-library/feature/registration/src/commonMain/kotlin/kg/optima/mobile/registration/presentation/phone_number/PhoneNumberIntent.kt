package kg.optima.mobile.registration.presentation.phone_number

import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.BaseMppIntent
import kg.optima.mobile.registration.domain.usecase.CheckPhoneNumberUseCase
import org.koin.core.component.inject

class PhoneNumberIntent(
	override val mppState: PhoneNumberState,
) : BaseMppIntent<CheckPhoneNumberInfo>() {

    private val phoneNumberValidator = PhoneNumberValidator
    private val checkPhoneNumberUseCase: CheckPhoneNumberUseCase by inject()

    fun onValueChanged(number: String) {
        phoneNumberValidator.validate(number).fold(
            fnL = { mppState.handle(CheckPhoneNumberInfo.Validation(false, it.message)) },
            fnR = { mppState.handle(CheckPhoneNumberInfo.Validation(true)) }
        )
    }

    fun phoneNumberEntered(number: String) {
        launchOperation(false) {
            checkPhoneNumberUseCase.execute(number).map {
                CheckPhoneNumberInfo.Check(
                    success = it.success,
                    referenceId = it.referenceId,
                    phoneNumber = number,
                    timeLeft = it.timeLeft,
                    message = it.message ?: ""
                )
            }
        }
    }
}