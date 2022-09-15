package kg.optima.mobile.registration.presentation.sms_code

sealed interface CheckSmsCodeInfo {

	class ReRequest(
		val success: Boolean,
		//TODO timeout
	) : CheckSmsCodeInfo

	class Check(
		val success: Boolean,
		val date: String,
		val accessToken: String,
		val personId: String
	) : CheckSmsCodeInfo

	class Timeout(
		val timeout: Int
	) : CheckSmsCodeInfo

	object EnableReRequest : CheckSmsCodeInfo


}