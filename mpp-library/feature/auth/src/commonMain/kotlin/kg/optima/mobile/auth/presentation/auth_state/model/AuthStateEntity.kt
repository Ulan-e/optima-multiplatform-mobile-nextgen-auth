package kg.optima.mobile.auth.presentation.auth_state.model

import kg.optima.mobile.auth.domain.usecase.login.GrantType

sealed interface AuthStateEntity {
	class ClientInfo(
		val isAuthorized: Boolean,
		val clientId: String?,
		val grantTypes: List<GrantType>,
	) : AuthStateEntity
}