package kg.optima.mobile.auth.data.api

import io.ktor.http.*
import kg.optima.mobile.auth.data.api.model.login.LoginResponse
import kg.optima.mobile.auth.data.api.model.login.UserAuthenticationRequest
import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.core.StringMap
import kg.optima.mobile.network.client.NetworkClient

class AuthApiImpl(
	networkClient: NetworkClient,
) : AuthApi(networkClient) {

	override suspend fun login(
		path: String,
		request: UserAuthenticationRequest,
	): BaseDto<LoginResponse> = post(
		path = path,
		headers = {
			append(HttpHeaders.AcceptLanguage, "ru-RU")
//			append(HttpHeaders.UserAgent, userAgent)
		},
		request = request,
	)
}
