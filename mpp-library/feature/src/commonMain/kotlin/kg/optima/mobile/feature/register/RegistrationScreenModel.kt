package kg.optima.mobile.feature.register

import kg.optima.mobile.core.navigation.ScreenModel

sealed interface RegistrationScreenModel : ScreenModel {
	object Agreement : RegistrationScreenModel

	object EnterPhone : RegistrationScreenModel

	class AcceptCode(
		val phoneNumber: String,
		val timeout: Int,
		val referenceId: String,
	) : RegistrationScreenModel

	object SelfConfirm : RegistrationScreenModel

	object ControlQuestion : RegistrationScreenModel
}