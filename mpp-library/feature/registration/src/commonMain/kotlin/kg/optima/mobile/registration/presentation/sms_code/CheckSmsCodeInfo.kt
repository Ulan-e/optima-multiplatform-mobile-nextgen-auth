package kg.optima.mobile.registration.presentation.sms_code

sealed interface CheckSmsCodeInfo {

	class Check(
		val success: Boolean,
	) : CheckSmsCodeInfo

	class OtpCheck(
		val success: Boolean,
	) : CheckSmsCodeInfo

	class TimeLeft(
		val timeout: Int
	) : CheckSmsCodeInfo



}