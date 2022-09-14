package kg.optima.mobile.feature.registration

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

	@Parcelize
	class CreatePassword(
		val hash: String = "",
		val questionId: String = "",
		val answer: String = "",
	) : RegistrationScreenModel
}