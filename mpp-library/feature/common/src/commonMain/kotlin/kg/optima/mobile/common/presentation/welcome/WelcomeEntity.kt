package kg.optima.mobile.common.presentation.welcome

import kg.optima.mobile.feature.auth.model.GrantType

sealed interface WelcomeEntity {
	class ClientInfo(
		val isAuthorized: Boolean,
		val clientId: String?,
		val grantTypes: List<GrantType>,
	) : WelcomeEntity

	object Register : WelcomeEntity
}