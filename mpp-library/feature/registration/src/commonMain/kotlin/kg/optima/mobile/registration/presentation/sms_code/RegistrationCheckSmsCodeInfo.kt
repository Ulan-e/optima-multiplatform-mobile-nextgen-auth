package kg.optima.mobile.registration.presentation.sms_code

import kg.optima.mobile.common.presentation.sms.CheckSmsCodeInfo

sealed interface RegistrationCheckSmsCodeInfo : CheckSmsCodeInfo {
	class Check(
		val success: Boolean,
	) : RegistrationCheckSmsCodeInfo

	class OtpCheck(
		val success: Boolean,
	) : RegistrationCheckSmsCodeInfo

}