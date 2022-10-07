package kg.optima.mobile.common.presentation

import kg.optima.mobile.base.presentation.BaseMppState

open class SmsCodeState : BaseMppState<CheckSmsCodeInfo>() {

	override fun handle(entity: CheckSmsCodeInfo) {
		when (entity) {
			is CheckSmsCodeInfo.TimeLeft ->
				setStateModel(SmsCodeStateModel.TimeLeft(entity.timeout))
			else -> Unit
		}
	}

	sealed interface SmsCodeStateModel : StateModel {
		class TimeLeft(val timeLeft: Int) : SmsCodeStateModel
	}

}
