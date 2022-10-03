package kg.optima.mobile.auth.presentation.login

import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfoUseCase
import kg.optima.mobile.auth.domain.usecase.login.GrantType
import kg.optima.mobile.auth.domain.usecase.login.LoginUseCase
import kg.optima.mobile.auth.presentation.login.model.LoginModel
import kg.optima.mobile.auth.presentation.login.utils.toUseCaseModel
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.BaseMppIntent
import org.koin.core.component.inject

class LoginIntent(
	override val mppState: LoginState,
) : BaseMppIntent<LoginModel>() {

	private val loginUseCase: LoginUseCase by inject()
	private val clientInfoUseCase: ClientInfoUseCase by inject()

	fun signIn(info: SignInInfo) {
		launchOperation {
			loginUseCase.execute(info.toUseCaseModel())
		}
	}

	fun getClientId() {
		launchOperation {
			clientInfoUseCase.execute(ClientInfoUseCase.Params).map {
				LoginModel.ClientId(it.clientId)
			}
		}
	}

	fun showBiometry() {
		launchOperation {
			clientInfoUseCase.execute(ClientInfoUseCase.Params).map {
				LoginModel.Biometry(enabled = it.grantTypes.contains(GrantType.Biometry))
			}
		}
	}

	sealed interface SignInInfo {
		class Password(
			val clientId: String,
			val password: String,
		) : SignInInfo

		class Pin(
			val pin: String,
		) : SignInInfo

		object Biometry : SignInInfo
	}
}
