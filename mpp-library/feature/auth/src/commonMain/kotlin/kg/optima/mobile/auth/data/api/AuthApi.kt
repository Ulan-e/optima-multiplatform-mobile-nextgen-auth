package kg.optima.mobile.auth.data.api

import kg.optima.mobile.auth.data.api.model.login.LoginResponse
import kg.optima.mobile.auth.data.api.model.login.UserAuthenticationRequest
import kg.optima.mobile.base.data.BaseApi
import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.core.StringMap
import kg.optima.mobile.network.client.NetworkClient

abstract class AuthApi(
	networkClient: NetworkClient,
) : BaseApi(networkClient) {

	override val baseUrl: String
		get() = "https://telebank3.optima24.kg:3080"

	abstract suspend fun login(
		path: String = "api/auth/session",
		request: UserAuthenticationRequest,
	): BaseDto<LoginResponse>
}