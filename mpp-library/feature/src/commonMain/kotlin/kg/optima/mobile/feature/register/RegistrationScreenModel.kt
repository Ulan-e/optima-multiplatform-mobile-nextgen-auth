package kg.optima.mobile.feature.register

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.core.navigation.ScreenModel

sealed interface RegistrationScreenModel : ScreenModel {
	@Parcelize
	object Agreement : RegistrationScreenModel

	@Parcelize
	object EnterPhone : RegistrationScreenModel

	@Parcelize
	class AcceptCode(
		val phoneNumber: String,
		val timeout: Int,
		val referenceId: String,
	) : RegistrationScreenModel

	@Parcelize
	object SelfConfirm : RegistrationScreenModel

	@Parcelize
	object ControlQuestion : RegistrationScreenModel
}