package kg.optima.mobile.common.presentation.welcome

import kg.optima.mobile.base.presentation.UiIntent
import kg.optima.mobile.common.presentation.welcome.model.WelcomeEntity
import kg.optima.mobile.feature.auth.component.AuthPreferences
import org.koin.core.component.inject

class WelcomeIntent(
	override val uiState: WelcomeState,
	private val authPreferences: AuthPreferences // TODO exclude to usecase
) : UiIntent<WelcomeEntity>() {

	override fun init() {
		if (authPreferences.isAuthorized)
			uiState.handle(WelcomeEntity.Login)
	}

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
