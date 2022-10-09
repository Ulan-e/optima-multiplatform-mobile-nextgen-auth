package kg.optima.mobile.common.presentation.sms

import kg.optima.mobile.base.presentation.BaseEntity

interface CheckSmsCodeInfo : BaseEntity {
	class TimeLeft(val timeout: Int) : CheckSmsCodeInfo
}