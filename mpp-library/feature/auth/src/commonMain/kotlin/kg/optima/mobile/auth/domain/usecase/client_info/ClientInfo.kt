package kg.optima.mobile.auth.domain.usecase.client_info

import kg.optima.mobile.auth.domain.usecase.login.GrantType

class ClientInfo(
	val isAuthorized: Boolean,
	val clientId: String?,
	val grantTypes: List<GrantType>
)