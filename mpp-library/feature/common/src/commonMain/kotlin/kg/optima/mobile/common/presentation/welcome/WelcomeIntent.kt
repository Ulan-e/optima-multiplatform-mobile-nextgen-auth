package kg.optima.mobile.common.presentation.welcome

import kg.optima.mobile.base.presentation.UiIntent
import kg.optima.mobile.common.presentation.welcome.model.WelcomeEntity


class WelcomeIntent(
	override val uiState: WelcomeState,
) : UiIntent<WelcomeEntity>() {

	fun login() =
		uiState.handle(WelcomeEntity.Login)

	fun register() =
		uiState.handle(WelcomeEntity.Register)

	fun openMap() =
		uiState.handle(WelcomeEntity.ButtonBlock.Map)

	fun openLanguages() =
		uiState.handle(WelcomeEntity.ButtonBlock.Languages)

	fun openRates() =
		uiState.handle(WelcomeEntity.ButtonBlock.Rates)

	fun openContacts() =
		uiState.handle(WelcomeEntity.ButtonBlock.Contacts)
}
