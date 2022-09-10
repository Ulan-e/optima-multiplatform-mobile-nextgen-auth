package kg.optima.mobile.common.presentation.welcome

import kg.optima.mobile.auth.domain.usecase.login.GrantType

sealed interface WelcomeEntity {
	class ClientInfo(
		val isAuthorized: Boolean,
		val clientId: String?,
		val grantTypes: List<GrantType>,
	) : WelcomeEntity

	object Register : WelcomeEntity
}