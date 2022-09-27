package kg.optima.mobile.network.const

enum class NetworkCode(val code: Int) {
	Success(0),
	ServiceUnavailable(-999),

	SmsCodeRequired(-10006),
	IncorrectCodeOrPassword(-100),
	UserBlocked(-201);

	companion object {
		fun byCode(code: Int) = when (code) {
			Success.code -> Success
			ServiceUnavailable.code -> ServiceUnavailable
			SmsCodeRequired.code -> SmsCodeRequired
			IncorrectCodeOrPassword.code -> IncorrectCodeOrPassword
			UserBlocked.code -> UserBlocked
			else -> null
		}
	}
}