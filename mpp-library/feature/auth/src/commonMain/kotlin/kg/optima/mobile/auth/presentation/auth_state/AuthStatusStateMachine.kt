package kg.optima.mobile.auth.presentation.auth_state

import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfo
import kg.optima.mobile.auth.domain.usecase.login.GrantType
import kg.optima.mobile.base.presentation.StateMachine

class AuthStatusStateMachine : StateMachine<ClientInfo>() {

	sealed interface AuthStatusState : State {
		val clientId: String?

		class Authorized(
			override val clientId: String,
			val grantTypes: List<GrantType>,
		) : AuthStatusState

		class NotAuthorized : AuthStatusState {
			override val clientId: String? = null
		}
	}

	override fun handle(entity: ClientInfo) {
		val state = if (entity.isAuthorized) {
			AuthStatusState.Authorized(
				clientId = entity.clientId.orEmpty(),
				grantTypes = entity.grantTypes,
			)
		} else {
			AuthStatusState.NotAuthorized()
		}

		setState(state)
	}
}