package kg.optima.mobile.auth.presentation.sms

import kg.optima.mobile.auth.presentation.sms.model.AuthCheckSmsCodeInfo
import kg.optima.mobile.common.presentation.CheckSmsCodeInfo
import kg.optima.mobile.common.presentation.SmsCodeState
import kg.optima.mobile.core.navigation.ScreenModel

class AuthSmsCodeState(
	private val nextScreenModel: ScreenModel,
) : SmsCodeState() {
	override fun handle(entity: CheckSmsCodeInfo) {
		when (entity) {
			AuthCheckSmsCodeInfo.Success -> setStateModel(StateModel.Navigate(nextScreenModel))
			else -> super.handle(entity)
		}
	}
}