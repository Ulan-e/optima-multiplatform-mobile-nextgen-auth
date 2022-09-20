package kg.optima.mobile.registration.presentation.sms_code

import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.registration.presentation.sms_code.utils.Timeouts

sealed interface CheckSmsCodeInfo {

	class ReRequest(
		val success: Boolean,
		//TODO timeout
	) : CheckSmsCodeInfo

	class Check(
		val success: Boolean,
		val message: String = emptyString,
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
				tryCount = 1,
				timeLeft = Timeouts.get(1).timeout
			)
		}
	}

	object TryDataSaved : CheckSmsCodeInfo


}