package kg.optima.mobile.auth.data.repository

import kg.optima.mobile.auth.data.api.AuthApi
import kg.optima.mobile.auth.data.api.model.jwt.JwtRefreshRequest
import kg.optima.mobile.auth.data.api.model.otp.AuthorizeOtpRequest
import kg.optima.mobile.auth.data.api.model.otp.SendOtpRequest
import kg.optima.mobile.auth.data.api.model.register.UserRegistrationRequest
import kg.optima.mobile.auth.data.api.model.resetPassword.PasswordResetRequest
import kg.optima.mobile.auth.data.api.model.user.UserCheckRequest
import kg.optima.mobile.base.data.BaseDataSource
import kg.optima.mobile.core.StringMap

class AuthRepositoryImpl(
	private val authApi: AuthApi,
) : AuthRepository, BaseDataSource() {

	override suspend fun login(params: StringMap) = apiCall {
		authApi.login(params)
	}

	override suspend fun checkOtp(body: AuthorizeOtpRequest) = apiCall {
		authApi.checkOtp(body)
	}

	override suspend fun sendOtp(body: SendOtpRequest) = apiCall {
		authApi.sendOtp(body)
	}

	override suspend fun user(userId: String) = apiCall {
		authApi.user(userId)
	}

	override suspend fun register(body: UserRegistrationRequest) = apiCall {
		authApi.register(body)
	}

	override suspend fun jwtRefresh(body: JwtRefreshRequest) = apiCall {
		authApi.jwtRefresh(body)
	}

	override suspend fun resetPassword(body: PasswordResetRequest) = apiCall {
		authApi.resetPassword(body)
	}

	override suspend fun userCheck(body: UserCheckRequest) = apiCall {
		authApi.userCheck(body)
	}

}
