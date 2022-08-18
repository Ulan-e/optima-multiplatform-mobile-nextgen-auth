package kg.optima.mobile.auth.presentation.auth_state

import kg.optima.mobile.auth.domain.usecase.login.GrantType
import kg.optima.mobile.auth.presentation.auth_state.model.AuthStateEntity
import kg.optima.mobile.base.presentation.StateMachine

class AuthStatusStateMachine : StateMachine<AuthStateEntity>() {

	sealed interface AuthStatusState : State {
		class Authorized(
			val clientId: String,
			val grantTypes: List<GrantType>,
		) : AuthStatusState

		class NotAuthorized : AuthStatusState {
			val clientId: String? = null
		}
	}

	override fun handle(entity: AuthStateEntity) {
		val state = when (entity) {
			is AuthStateEntity.ClientInfo -> {
				if (entity.isAuthorized) {
					AuthStatusState.Authorized(
						clientId = entity.clientId.orEmpty(),
						grantTypes = entity.grantTypes,
					)
				} else {
					AuthStatusState.NotAuthorized()
				}
			}
		}

		setState(state)
	}
}