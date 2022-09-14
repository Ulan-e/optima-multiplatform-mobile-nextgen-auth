package kg.optima.mobile.feature.auth

import kg.optima.mobile.core.navigation.ScreenModel

sealed interface AuthScreenModel : ScreenModel {
	val nextScreenModel: ScreenModel

	class Login(override val nextScreenModel: ScreenModel) : AuthScreenModel

	class PinEnter(
		val showBiometry: Boolean,
		override val nextScreenModel: ScreenModel,
	) : AuthScreenModel

	class PinSet(override val nextScreenModel: ScreenModel) : AuthScreenModel {
		override val dropBackStack: Boolean = true
	}
}