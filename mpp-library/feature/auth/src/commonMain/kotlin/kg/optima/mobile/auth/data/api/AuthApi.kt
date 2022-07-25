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
import kg.optima.mobile.base.data.BaseApi
import kg.optima.mobile.network.client.NetworkClient

abstract class AuthApi(
    networkClient: NetworkClient,
    baseUrl: String = "",
) : BaseApi(networkClient, baseUrl) {

    abstract suspend fun checkOtp(
        body: AuthorizeOtpRequest,
        httpMethod: HttpMethod = HttpMethod.Post,
        path: String = "auth-api/v1/auth-otp",
    ): AuthorizeOtpResponse?

    abstract suspend fun sendOtp(
        body: SendOtpRequest,
        httpMethod: HttpMethod = HttpMethod.Post,
        path: String = "auth-api/v1/send-otp",
    ): SendOtpResponse?

    abstract suspend fun user(
        userId: String,
        httpMethod: HttpMethod = HttpMethod.Get,
        path: String = "auth-api/v1/get-user/$userId",
    ): UserResponse?

    abstract suspend fun login(
        body: UserAuthenticationRequest,
        httpMethod: HttpMethod = HttpMethod.Post,
        path: String = "auth-api/v1/authenticate",
    ): UserAuthenticationResponse?

    abstract suspend fun register(
        body: UserRegistrationRequest,
        httpMethod: HttpMethod = HttpMethod.Post,
        path: String = "auth-api/v1/register",
    ): UserRegistrationResponse?


    abstract suspend fun jwtRefresh(
        body: JwtRefreshRequest,
        httpMethod: HttpMethod = HttpMethod.Post,
        path: String = "auth-api/v1/jwt-refresh",
    ): JwtRefreshResponse?

    abstract suspend fun resetPassword(
        body: PasswordResetRequest,
        httpMethod: HttpMethod = HttpMethod.Post,
        path: String = "auth-api/v1/password-reset",
    ): PasswordResetResponse?

    abstract suspend fun userCheck(
        body: UserCheckRequest,
        httpMethod: HttpMethod = HttpMethod.Post,
        path: String = "auth-api/v1/user-check",
    ): UserCheckResponse?
}