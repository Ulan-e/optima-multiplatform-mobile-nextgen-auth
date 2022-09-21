package kg.optima.mobile.registration.presentation.phone_number

import kg.optima.mobile.base.utils.emptyString

sealed interface CheckPhoneNumberInfo {

	class Validation(
		val success: Boolean,
		val message: String = emptyString,
	) : CheckPhoneNumberInfo

	class PhoneNumber(
		val phoneNumber: String,
	) : CheckPhoneNumberInfo

}