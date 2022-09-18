package kg.optima.mobile.registration.presentation.phone_number

import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.Intent
import kg.optima.mobile.registration.domain.CheckPhoneNumberUseCase
import kotlinx.coroutines.delay
import kg.optima.mobile.registration.domain.usecase.CheckPhoneNumberUseCase
import org.koin.core.component.inject

class PhoneNumberIntent(
	override val state: PhoneNumberState,
) : Intent<CheckPhoneNumberInfo>() {

	private val checkPhoneNumberUseCase: CheckPhoneNumberUseCase by inject()
	private val phoneNumberValidator = PhoneNumberValidator

	fun onValueChanged(number: String) {
		phoneNumberValidator.validate(number).fold(
			fnL = { state.handle(CheckPhoneNumberInfo.Validation(false, it.message)) },
			fnR = { state.handle(CheckPhoneNumberInfo.Validation(true)) }
		)
	}

	fun phoneNumberEntered(number: String) {
		launchOperation(withLoading = false) {
			checkPhoneNumberUseCase.execute(number).map {
				CheckPhoneNumberInfo.Check(
					success = it.success,
					phoneNumber = number,
					referenceId = it.referenceId,
				)
			}
		}
	}
}