package kg.optima.mobile.auth.data.repository

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
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.core.error.Failure


interface AuthRepository {

    suspend fun checkOtp(body: AuthorizeOtpRequest): Either<Failure, AuthorizeOtpResponse?>

    suspend fun sendOtp(body: SendOtpRequest): Either<Failure, SendOtpResponse?>

    suspend fun user(userId: String): Either<Failure, UserResponse?>

    suspend fun login(body: UserAuthenticationRequest): Either<Failure, UserAuthenticationResponse?>

    suspend fun register(body: UserRegistrationRequest): Either<Failure, UserRegistrationResponse?>

    suspend fun jwtRefresh(body: JwtRefreshRequest): Either<Failure, JwtRefreshResponse?>

    suspend fun resetPassword(body: PasswordResetRequest): Either<Failure, PasswordResetResponse?>

    suspend fun userCheck(body: UserCheckRequest): Either<Failure, UserCheckResponse?>
}
