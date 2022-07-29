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
import kg.optima.mobile.core.StringMap
import kg.optima.mobile.network.client.NetworkClient

class AuthApiImpl(
	networkClient: NetworkClient,
) : AuthApi(networkClient) {

	override suspend fun login(
		params: StringMap,
		path: String,
	): UserAuthenticationResponse = post(
		path = path,
		params = params
	)

	override suspend fun checkOtp(
		request: AuthorizeOtpRequest,
		httpMethod: HttpMethod,
		path: String,
	): AuthorizeOtpResponse = request(
		path = path,
		body = request to AuthorizeOtpRequest.serializer(),
		httpMethod = httpMethod,
	)

	override suspend fun sendOtp(
		request: SendOtpRequest,
		httpMethod: HttpMethod,
		path: String,
	): SendOtpResponse = request(
		path = path,
		body = request to SendOtpRequest.serializer(),
		httpMethod = httpMethod,
	)

	override suspend fun user(
		userId: String,
		httpMethod: HttpMethod,
		path: String,
	): UserResponse = request<Any, UserResponse>(
		path = path,
		httpMethod = httpMethod,
	)

	override suspend fun register(
		request: UserRegistrationRequest,
		httpMethod: HttpMethod,
		path: String,
	): UserRegistrationResponse = request(
		path = path,
		body = request to UserRegistrationRequest.serializer(),
		httpMethod = httpMethod,
	)

	override suspend fun jwtRefresh(
		request: JwtRefreshRequest,
		httpMethod: HttpMethod,
		path: String,
	): JwtRefreshResponse = request(
		path = path,
		body = request to JwtRefreshRequest.serializer(),
		httpMethod = httpMethod,
	)

	override suspend fun resetPassword(
		request: PasswordResetRequest,
		httpMethod: HttpMethod,
		path: String,
	): PasswordResetResponse = request(
		path = path,
		body = request to PasswordResetRequest.serializer(),
		httpMethod = httpMethod,
	)

	override suspend fun userCheck(
		request: UserCheckRequest,
		httpMethod: HttpMethod,
		path: String,
	): UserCheckResponse? = request(
		path = path,
		body = request to UserCheckRequest.serializer(),
		httpMethod = httpMethod,
	)
}
