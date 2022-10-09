package kg.optima.mobile.auth.presentation.login

import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfoUseCase
import kg.optima.mobile.auth.domain.usecase.login.LoginUseCase
import kg.optima.mobile.auth.presentation.login.model.LoginEntity
import kg.optima.mobile.auth.presentation.login.utils.toEntity
import kg.optima.mobile.auth.presentation.login.utils.toUseCaseModel
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.UiIntent
import org.koin.core.component.inject

@Suppress("UNCHECKED_CAST")
open class LoginIntent(
	override val uiState: LoginState,
) : UiIntent<LoginEntity>() {

	private val loginUseCase: LoginUseCase by inject()
	private val clientInfoUseCase: ClientInfoUseCase by inject()

	override fun init() {
		launchOperation {
			clientInfoUseCase.execute(ClientInfoUseCase.Params).map {
				LoginEntity.ClientInfo(
					clientId = it.clientId,
					isAuthorized = it.isAuthorized,
					pinEnabled = it.pinEnabled,
					biometryEnabled = it.biometryEnabled,
				)
			}
		}
	}

	fun signIn(info: SignInInfo) {
		launchOperation {
			loginUseCase.execute(info.toUseCaseModel()).map {
				it.toEntity()
			}
		}
	}

	sealed interface SignInInfo {
		data class Password(
			val clientId: String,
			val password: String,
			val smsCode: String? = null,
		) : SignInInfo

		class Pin(
			val pin: String,
		) : SignInInfo

		object Biometry : SignInInfo
	}
}
