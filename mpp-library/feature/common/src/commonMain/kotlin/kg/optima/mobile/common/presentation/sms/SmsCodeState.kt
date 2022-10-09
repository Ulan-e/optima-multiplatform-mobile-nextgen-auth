package kg.optima.mobile.common.presentation.sms

import kg.optima.mobile.base.presentation.UiState

open class SmsCodeState : UiState<CheckSmsCodeInfo>() {

	override fun handle(entity: CheckSmsCodeInfo) {
		when (entity) {
			is CheckSmsCodeInfo.TimeLeft ->
				setStateModel(SmsCodeStateModel.TimeLeft(entity.timeout))
			else -> Unit
		}
	}

	sealed interface SmsCodeStateModel : Model {
		class TimeLeft(val timeLeft: Int) : SmsCodeStateModel
	}

}
