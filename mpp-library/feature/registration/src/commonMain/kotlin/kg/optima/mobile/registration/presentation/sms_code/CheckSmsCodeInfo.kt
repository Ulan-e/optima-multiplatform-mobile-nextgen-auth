package kg.optima.mobile.registration.presentation.sms_code

import kg.optima.mobile.base.utils.emptyString

sealed interface CheckSmsCodeInfo {

	class ReRequest(
		val success: Boolean,
		//TODO timeout
	) : CheckSmsCodeInfo

	class Check(
		val success: Boolean,
		val message: String = emptyString,
	) : CheckSmsCodeInfo

	class Timeout(
		val timeout: Int
	) : CheckSmsCodeInfo

	object EnableReRequest : CheckSmsCodeInfo


}