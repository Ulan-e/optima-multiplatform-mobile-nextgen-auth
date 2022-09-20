package kg.optima.mobile.registration.presentation.phone_number

import kg.optima.mobile.base.presentation.Intent

class PhoneNumberIntent(
	override val state: PhoneNumberState,
) : Intent<CheckPhoneNumberInfo>() {

	private val phoneNumberValidator = PhoneNumberValidator

	fun onValueChanged(number: String) {
		phoneNumberValidator.validate(number).fold(
			fnL = { state.handle(CheckPhoneNumberInfo.Validation(false, it.message)) },
			fnR = { state.handle(CheckPhoneNumberInfo.Validation(true)) }
		)
	}

	fun phoneNumberEntered(number: String) {
		state.handle(CheckPhoneNumberInfo.PhoneNumber(number))
	}
}