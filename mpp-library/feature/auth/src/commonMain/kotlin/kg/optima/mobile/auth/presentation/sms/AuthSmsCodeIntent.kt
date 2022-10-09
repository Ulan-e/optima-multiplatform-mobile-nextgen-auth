package kg.optima.mobile.auth.presentation.sms

import kg.optima.mobile.auth.domain.usecase.login.LoginUseCase
import kg.optima.mobile.auth.presentation.sms.model.AuthCheckSmsCodeInfo
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.common.presentation.sms.SmsCodeIntent
import kg.optima.mobile.feature.auth.model.SignInInfo
import org.koin.core.component.inject

class AuthSmsCodeIntent(state: AuthSmsCodeState) : SmsCodeIntent(state) {

	private val loginUseCase: LoginUseCase by inject()

	private fun signIn(info: SignInInfo) {
		launchOperation {
			loginUseCase.execute(info.toLoginUseCaseModel()).map {
				AuthCheckSmsCodeInfo.Success
			}
		}
	}

	fun onInputCompleted(info: SignInInfo, code: String) = signIn(info.copy(smsCode = code))

	fun onResendSms(info: SignInInfo) = signIn(info)

	private fun SignInInfo.toLoginUseCaseModel() =
		LoginUseCase.Params.Password(clientId, password, smsCode)
}