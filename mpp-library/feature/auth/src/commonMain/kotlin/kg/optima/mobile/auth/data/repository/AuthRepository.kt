package kg.optima.mobile.auth.data.repository

import kg.optima.mobile.auth.data.api.model.login.LoginResponse
import kg.optima.mobile.auth.data.api.model.login.UserAuthenticationRequest
import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.core.error.Failure


interface AuthRepository {

	suspend fun login(request: UserAuthenticationRequest): Either<Failure, BaseDto<LoginResponse>>

	suspend fun keepAlive(sessionId: String): Either<Failure, BaseDto<String>>
}
