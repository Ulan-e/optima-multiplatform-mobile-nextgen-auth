package kg.optima.mobile.auth.presentation.login.utils

import kg.optima.mobile.auth.domain.usecase.login.LoginUseCase
import kg.optima.mobile.auth.presentation.login.LoginIntent

fun LoginIntent.SignInInfo.toUseCaseModel(): LoginUseCase.Params {
	return when (val model = this) {
		LoginIntent.SignInInfo.Biometry -> LoginUseCase.Params.Biometry
		is LoginIntent.SignInInfo.Password -> LoginUseCase.Params.Password(
			clientId = model.clientId,
			password = model.password,
			smsCode = model.smsCode,
		)
		is LoginIntent.SignInInfo.Pin -> LoginUseCase.Params.Pin(model.pin)
	}
}