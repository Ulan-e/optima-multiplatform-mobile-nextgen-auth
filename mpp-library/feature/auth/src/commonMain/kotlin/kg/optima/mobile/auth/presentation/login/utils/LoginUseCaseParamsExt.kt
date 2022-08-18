package kg.optima.mobile.auth.presentation.login.utils

import kg.optima.mobile.auth.domain.usecase.login.LoginUseCase
import kg.optima.mobile.auth.presentation.login.LoginIntentHandler

fun LoginIntentHandler.LoginIntent.SignIn.toUseCaseModel(): LoginUseCase.Params {
	return when (val model = this) {
		LoginIntentHandler.LoginIntent.SignIn.Biometry -> LoginUseCase.Params.Biometry
		is LoginIntentHandler.LoginIntent.SignIn.Password -> LoginUseCase.Params.Password(
			clientId = model.clientId,
			password = model.password,
		)
		is LoginIntentHandler.LoginIntent.SignIn.Pin -> LoginUseCase.Params.Pin(model.pin)
	}
}