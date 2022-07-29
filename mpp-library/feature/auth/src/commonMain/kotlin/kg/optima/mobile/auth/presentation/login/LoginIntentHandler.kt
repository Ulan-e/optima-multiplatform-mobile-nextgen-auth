package kg.optima.mobile.auth.presentation.login

import kg.optima.mobile.auth.data.api.model.login.UserAuthenticationResponse
import kg.optima.mobile.auth.domain.CryptographyUtils
import kg.optima.mobile.auth.domain.usecase.login.GrantType
import kg.optima.mobile.auth.domain.usecase.login.LoginUseCase
import kg.optima.mobile.base.presentation.IntentHandler
import org.koin.core.component.inject

class LoginIntentHandler(
	override val stateMachine: LoginStateMachine,
) : IntentHandler<LoginIntentHandler.LoginIntent, UserAuthenticationResponse>() {

	class LoginIntent(
		val clientId: String,
		val password: String,
		val grantType: GrantType,
	) : Intent

	private val loginUseCase: LoginUseCase by inject()

	override fun dispatch(intent: LoginIntent) {
		launchOperation<UserAuthenticationResponse> {
			val model = LoginUseCase.Params(
				clientId = intent.clientId,
				password = CryptographyUtils.getHash(intent.password),
				grantType = intent.grantType,
			)

			loginUseCase.execute(model)
		}
	}
}
