package kg.optima.mobile.auth.data.api

import kg.optima.mobile.auth.data.api.model.login.LoginResponse
import kg.optima.mobile.auth.data.api.model.login.UserAuthenticationRequest
import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.network.client.NetworkClient

class AuthApiImpl(
	networkClient: NetworkClient,
) : AuthApi(networkClient) {

	override suspend fun login(
		path: String,
		request: UserAuthenticationRequest,
	): BaseDto<LoginResponse> = post(
		path = path,
		request = request,
	)

	override suspend fun keepAlive(
		path: String,
		sessionId: String,
	): BaseDto<String> = post(
		path = path,
		headers = {
			append("XX-TB-AuthToken", sessionId)
		},
		request = null,
	)
}
