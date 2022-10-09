package kg.optima.mobile.auth.presentation.pin_enter

import kg.optima.mobile.auth.presentation.login.LoginIntent
import kg.optima.mobile.auth.presentation.login.model.LoginModel
import kg.optima.mobile.base.presentation.UiIntent

open class PinEnterIntent(
	override val uiState: PinEnterState,
) : LoginIntent(uiState) {

	fun logout() {
		// TODO logout
	}
}
