package kg.optima.mobile.common.presentation

interface CheckSmsCodeInfo {
	class TimeLeft(val timeout: Int) : CheckSmsCodeInfo
}