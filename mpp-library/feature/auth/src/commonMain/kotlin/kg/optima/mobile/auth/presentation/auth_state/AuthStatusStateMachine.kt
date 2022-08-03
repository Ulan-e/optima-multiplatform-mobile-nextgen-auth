package kg.optima.mobile.auth.presentation.auth_state

import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfo
import kg.optima.mobile.base.presentation.StateMachine

class AuthStatusStateMachine : StateMachine<ClientInfo>() {

	sealed interface AuthStatusState : State {
		class ClientInfo(
			val isAuthorized: Boolean,
			val clientId: String?,
		) : AuthStatusState
	}

	override fun handle(entity: ClientInfo) {
		setState(AuthStatusState.ClientInfo(
			isAuthorized = entity.isAuthorized,
			clientId = entity.clientId
		))
	}
}