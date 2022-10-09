package kg.optima.mobile.auth.presentation.login.utils

import kg.optima.mobile.auth.domain.usecase.login.LoginSignInResult
import kg.optima.mobile.auth.presentation.login.model.LoginEntity

fun LoginSignInResult.toEntity(): LoginEntity.SignInResult = when (this) {
	LoginSignInResult.Error -> LoginEntity.SignInResult.Error
	is LoginSignInResult.IncorrectData -> LoginEntity.SignInResult.IncorrectData(message)
	is LoginSignInResult.SmsCodeRequired -> LoginEntity.SignInResult.SmsCodeRequired(otpModel)
	is LoginSignInResult.SuccessAuth -> LoginEntity.SignInResult.SuccessAuth(firstAuth)
	LoginSignInResult.UserBlocked -> LoginEntity.SignInResult.UserBlocked
}