package kg.optima.mobile.auth.domain.usecase.client_info

class ClientInfo(
	val clientId: String,
	val isAuthorized: Boolean,
	val pinEnabled: Boolean,
	val biometryEnabled: Boolean,
)