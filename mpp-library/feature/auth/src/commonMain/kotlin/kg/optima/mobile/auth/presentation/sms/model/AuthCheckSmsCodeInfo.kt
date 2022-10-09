package kg.optima.mobile.auth.presentation.sms.model

import kg.optima.mobile.common.presentation.sms.CheckSmsCodeInfo

sealed interface AuthCheckSmsCodeInfo : CheckSmsCodeInfo {
	object Success : AuthCheckSmsCodeInfo


}