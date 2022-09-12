package kg.optima.mobile.registration.presentation.phone_number

import kg.optima.mobile.base.utils.emptyString

sealed interface CheckPhoneNumberInfo {

	val success: Boolean

	class Validation(
		override val success: Boolean,
		val message: String = emptyString,
	) : CheckPhoneNumberInfo

	class Check(
		override val success: Boolean,
		val phoneNumber: String,
	) : CheckPhoneNumberInfo
}