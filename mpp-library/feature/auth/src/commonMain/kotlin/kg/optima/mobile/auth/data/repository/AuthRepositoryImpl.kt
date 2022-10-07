package kg.optima.mobile.auth.data.repository

import kg.optima.mobile.auth.data.api.AuthApi
import kg.optima.mobile.auth.data.api.model.login.UserAuthenticationRequest
import kg.optima.mobile.base.data.BaseDataSource
import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.core.error.Failure

class AuthRepositoryImpl(
	private val authApi: AuthApi,
) : AuthRepository, BaseDataSource() {

	override suspend fun login(request: UserAuthenticationRequest) = apiCall {
		authApi.login(request = request)
	}

	override suspend fun keepAlive(sessionId: String): Either<Failure, BaseDto<String>> = apiCall {
		authApi.keepAlive(sessionId = sessionId)
	}

}
