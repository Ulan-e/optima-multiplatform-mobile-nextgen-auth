package kg.optima.mobile.registration.presentation.sms_code

sealed interface CheckSmsCodeInfo {

	class Check(
		val success: Boolean,
		val referenceId: String,
	) : CheckSmsCodeInfo

	class OtpCheck(
		val success: Boolean,
	) : CheckSmsCodeInfo

	class TimeLeft(
		val timeout: Int
	) : CheckSmsCodeInfo

	class TriesData(
		val tryCount: Int,
		val timeLeft: Int,
	) : CheckSmsCodeInfo {
		companion object {
			val FIRST_TRY = TriesData(
				tryCount = 0,
				timeLeft = 0
			)
		}
	}

	object TryDataSaved : CheckSmsCodeInfo


}