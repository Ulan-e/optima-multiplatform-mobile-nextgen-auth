package kg.optima.mobile.registration.presentation.sms_code

sealed interface CheckSmsCodeInfo {

	class ReRequest(
		val success: Boolean,
		//TODO timeout
	) : CheckSmsCodeInfo

	class Check(
		val success: Boolean,
	) : CheckSmsCodeInfo

	class Timeout(
		val timeout: Int
	) : CheckSmsCodeInfo

	object EnableReRequest : CheckSmsCodeInfo


}