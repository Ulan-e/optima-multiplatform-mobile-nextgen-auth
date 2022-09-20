package kg.optima.mobile.core.common

object Constants {
	const val MAX_LENGTH_INPUT = 999
	const val DEBOUNCE_WAIT_MS = 300L

	const val PIN_LENGTH = 4
	const val OTP_LENGTH = 4

	const val PHONE_NUMBER_LENGTH = 9
	const val PHONE_NUMBER_CODE = "+996 "
	const val PHONE_NUMBER_MASK = "$PHONE_NUMBER_CODE(000) 000 000"

	const val PASSWORD_LENGTH = 8
	const val CONTROL_ANSWER_LENGTH = 5
	const val OTP_MAX_TRIES = 3

	const val OTP_INVALID_ERROR_CODE: String = "111"
	const val OTP_NO_TRIES: String = "222"
}