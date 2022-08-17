package kg.optima.mobile.auth.data.api.model.login

import kg.optima.mobile.auth.domain.usecase.login.GrantType

class UserAuthenticationRequest(
    val grantType: GrantType,
    val clientId: String,
    val password: String,
) {
	val map: Map<String, String>
		get() = mapOf(
            "client_id" to "optima-24",
            "grant_type" to grantType.method,
            "username" to clientId,
            "password" to password,
		)
}