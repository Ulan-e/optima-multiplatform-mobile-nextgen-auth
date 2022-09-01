package kg.optima.mobile.feature.auth

import kg.optima.mobile.core.navigation.ScreenModel

sealed interface AuthScreenModel : ScreenModel {
	object Login : AuthScreenModel
	object PinSet : AuthScreenModel
	class PinEnter(val showBiometry: Boolean) : AuthScreenModel
}