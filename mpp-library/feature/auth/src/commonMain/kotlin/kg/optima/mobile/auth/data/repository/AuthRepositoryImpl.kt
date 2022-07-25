package kg.optima.mobile.auth.data.repository

import kg.optima.mobile.auth.data.api.AuthApi
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
import kg.optima.mobile.base.data.BaseDataSource
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.core.error.Failure


class AuthRepositoryImpl(
    private val authApi: AuthApi
) : AuthRepository, BaseDataSource() {

    override suspend fun checkOtp(
        body: AuthorizeOtpRequest
    ): Either<Failure, AuthorizeOtpResponse?> {
        return apiCall(
            call = {
                authApi.checkOtp(body)
            }
        )
    }

    override suspend fun sendOtp(
        body: SendOtpRequest
    ): Either<Failure, SendOtpResponse?> {
        return apiCall(
            call = {
                authApi.sendOtp(body)
            }
        )
    }

    override suspend fun user(userId: String): Either<Failure, UserResponse?> {
        return apiCall(
            call = {
                authApi.user(userId)
            }
        )
    }

    override suspend fun login(
        body: UserAuthenticationRequest
    ): Either<Failure, UserAuthenticationResponse?> {
        return apiCall(
            call = {
                authApi.login(body)
            }
        )
    }

    override suspend fun register(
        body: UserRegistrationRequest
    ): Either<Failure, UserRegistrationResponse?> {
        return apiCall(
            call = {
                authApi.register(body)
            }
        )
    }

    override suspend fun jwtRefresh(
        body: JwtRefreshRequest
    ): Either<Failure, JwtRefreshResponse?> {
        return apiCall(
            call = {
                authApi.jwtRefresh(body)
            }
        )
    }

    override suspend fun resetPassword(
        body: PasswordResetRequest
    ): Either<Failure, PasswordResetResponse?> {
        return apiCall(
            call = {
                authApi.resetPassword(body)
            }
        )
    }

    override suspend fun userCheck(body: UserCheckRequest): Either<Failure, UserCheckResponse?> {
        return apiCall(
            call = {
                authApi.userCheck(body)
            }
        )
    }

}
