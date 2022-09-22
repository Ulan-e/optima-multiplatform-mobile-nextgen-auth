package kg.optima.mobile.auth.data.repository

import kg.optima.mobile.auth.data.api.AuthApi
import kg.optima.mobile.auth.data.api.model.login.UserAuthenticationRequest
import kg.optima.mobile.base.data.BaseDataSource

class AuthRepositoryImpl(
	private val authApi: AuthApi,
) : AuthRepository, BaseDataSource() {

	override suspend fun login(request: UserAuthenticationRequest) = apiCall {
		authApi.login(request = request)
	}

}
