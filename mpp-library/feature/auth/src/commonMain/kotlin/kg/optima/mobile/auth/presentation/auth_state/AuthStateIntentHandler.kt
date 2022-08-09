package kg.optima.mobile.auth.presentation.auth_state

import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfo
import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfoUseCase
import kg.optima.mobile.base.presentation.IntentHandler
import org.koin.core.component.inject

class AuthStateIntentHandler(
	override val stateMachine: AuthStatusStateMachine,
) : IntentHandler<AuthStateIntentHandler.CheckIsAuthorizedIntent, ClientInfo>() {

	private val clientInfoUseCase: ClientInfoUseCase by inject()

	override fun dispatch(intent: CheckIsAuthorizedIntent) {
		launchOperation {
			clientInfoUseCase.execute(ClientInfoUseCase.Params)
		}
	}

	object CheckIsAuthorizedIntent : Intent
}
