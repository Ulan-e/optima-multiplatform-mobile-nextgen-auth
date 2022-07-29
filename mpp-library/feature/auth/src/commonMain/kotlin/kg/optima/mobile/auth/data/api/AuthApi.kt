package kg.optima.mobile.auth.data.api

import io.ktor.http.*
import kg.optima.mobile.auth.data.api.model.jwt.JwtRefreshRequest
import kg.optima.mobile.auth.data.api.model.jwt.JwtRefreshResponse
import kg.optima.mobile.auth.data.api.model.login.UserAuthenticationResponse
import kg.optima.mobile.auth.data.api.model.otp.AuthorizeOtpRequest
import kg.optima.mobile.auth.data.api.model.otp.AuthorizeOtpResponse
import kg.optima.mobile.auth.data.api.model.otp.SendOtpRequest
import kg.optima.mobile.auth.data.api.model.otp.SendOtpResponse
import kg.optima.mobile.auth.data.api.model.register.UserRegistrationRequest
import kg.optima.mobile.auth.data.api.model.register.UserRegistrationResponse
import kg.optima.mobile.auth.data.api.model.resetPassword.PasswordResetRequest
import kg.optima.mobile.auth.data.api.model.resetPassword.PasswordResetResponse
import kg.optima.mobile.auth.data.api.model.user.UserCheckRequest
import kg.optima.mobile.auth.data.api.model.user.UserCheckResponse
import kg.optima.mobile.auth.data.api.model.user.UserResponse
import kg.optima.mobile.base.data.BaseApi
import kg.optima.mobile.core.StringMap
import kg.optima.mobile.network.client.NetworkClient

abstract class AuthApi(
    networkClient: NetworkClient,
) : BaseApi(networkClient) {

	override val baseUrl: String
		get() = "http://10.185.233.253:8080"

	abstract suspend fun login(
		params: StringMap,
		path: String = "realms/Optima24/protocol/openid-connect/token",
	): UserAuthenticationResponse

	abstract suspend fun checkOtp(
		request: AuthorizeOtpRequest,
		httpMethod: HttpMethod = HttpMethod.Post,
		path: String = "auth-api/v1/auth-otp",
	): AuthorizeOtpResponse

	abstract suspend fun sendOtp(
		request: SendOtpRequest,
		httpMethod: HttpMethod = HttpMethod.Post,
		path: String = "auth-api/v1/send-otp",
	): SendOtpResponse

	abstract suspend fun user(
		userId: String,
		httpMethod: HttpMethod = HttpMethod.Get,
		path: String = "auth-api/v1/get-user/$userId",
	): UserResponse

	abstract suspend fun register(
		request: UserRegistrationRequest,
		httpMethod: HttpMethod = HttpMethod.Post,
		path: String = "auth-api/v1/register",
	): UserRegistrationResponse


	abstract suspend fun jwtRefresh(
		request: JwtRefreshRequest,
		httpMethod: HttpMethod = HttpMethod.Post,
		path: String = "auth-api/v1/jwt-refresh",
	): JwtRefreshResponse

	abstract suspend fun resetPassword(
		request: PasswordResetRequest,
		httpMethod: HttpMethod = HttpMethod.Post,
		path: String = "auth-api/v1/password-reset",
	): PasswordResetResponse

	abstract suspend fun userCheck(
		request: UserCheckRequest,
		httpMethod: HttpMethod = HttpMethod.Post,
		path: String = "auth-api/v1/user-check",
	): UserCheckResponse?
}