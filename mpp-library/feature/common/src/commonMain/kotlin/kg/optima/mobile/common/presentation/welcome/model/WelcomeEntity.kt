package kg.optima.mobile.common.presentation.welcome.model

import kg.optima.mobile.base.presentation.BaseEntity

sealed interface WelcomeEntity : BaseEntity {
	object Login: WelcomeEntity

	object Register : WelcomeEntity

	enum class ButtonBlock : WelcomeEntity {
		Map, Languages, Rates, Contacts;
	}
}