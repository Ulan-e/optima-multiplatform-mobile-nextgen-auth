package kg.optima.mobile.auth.data.api

import io.ktor.http.*
import kg.optima.mobile.auth.data.api.model.jwt.JwtRefreshRequest
import kg.optima.mobile.auth.data.api.model.jwt.JwtRefreshResponse
import kg.optima.mobile.auth.data.api.model.login.UserAuthenticationRequest
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
import kg.optima.mobile.network.client.NetworkClient

class AuthApiImpl(
    networkClient: NetworkClient,
) : AuthApi(networkClient) {

    override suspend fun checkOtp(
        body: AuthorizeOtpRequest,
        httpMethod: HttpMethod,
        path: String,
    ): AuthorizeOtpResponse? = request(
        path = path,
        body = body,
        serializer = AuthorizeOtpRequest.serializer(),
        httpMethod = httpMethod,
    )

    override suspend fun sendOtp(
        body: SendOtpRequest,
        httpMethod: HttpMethod,
        path: String,
    ): SendOtpResponse? = request(
        path = path,
        body = body,
        serializer = SendOtpRequest.serializer(),
        httpMethod = httpMethod,
    )

    override suspend fun user(
        userId: String,
        httpMethod: HttpMethod,
        path: String,
    ): UserResponse? = request<Any, UserResponse>(
        // TODO refactor
        path = path,
        httpMethod = httpMethod,
    )

    override suspend fun login(
        body: UserAuthenticationRequest,
        httpMethod: HttpMethod,
        path: String,
    ): UserAuthenticationResponse? = request(
        path = path,
        body = body,
        serializer = UserAuthenticationRequest.serializer(),
        httpMethod = httpMethod,
    )

    override suspend fun register(
        body: UserRegistrationRequest,
        httpMethod: HttpMethod,
        path: String,
    ): UserRegistrationResponse? = request(
        path = path,
        body = body,
        serializer = UserRegistrationRequest.serializer(),
        httpMethod = httpMethod,
    )

    override suspend fun jwtRefresh(
        body: JwtRefreshRequest,
        httpMethod: HttpMethod,
        path: String,
    ): JwtRefreshResponse? = request(
        path = path,
        body = body,
        serializer = JwtRefreshRequest.serializer(),
        httpMethod = httpMethod,
    )

    override suspend fun resetPassword(
        body: PasswordResetRequest,
        httpMethod: HttpMethod,
        path: String,
    ): PasswordResetResponse? = request(
        path = path,
        body = body,
        serializer = PasswordResetRequest.serializer(),
        httpMethod = httpMethod,
    )

    override suspend fun userCheck(
        body: UserCheckRequest,
        httpMethod: HttpMethod,
        path: String,
    ): UserCheckResponse? = request(
        path = path,
        body = body,
        serializer = UserCheckRequest.serializer(),
        httpMethod = httpMethod,
    )
}
