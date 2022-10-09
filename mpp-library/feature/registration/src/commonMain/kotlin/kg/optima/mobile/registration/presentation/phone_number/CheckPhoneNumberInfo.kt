package kg.optima.mobile.registration.presentation.phone_number

import kg.optima.mobile.base.presentation.BaseEntity
import kg.optima.mobile.base.utils.emptyString

sealed interface CheckPhoneNumberInfo : BaseEntity {

	class Validation(
		val success: Boolean,
		val message: String = emptyString,
	) : CheckPhoneNumberInfo

	class Check(
		val success: Boolean,
		val phoneNumber: String,
		val referenceId: String,
		val timeLeft: Long,
		val message: String
	) : CheckPhoneNumberInfo

	object NavigateToMain : CheckPhoneNumberInfo

}