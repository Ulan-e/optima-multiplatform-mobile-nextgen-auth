package kg.optima.mobile.auth.presentation.auth_state

import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfoUseCase
import kg.optima.mobile.auth.presentation.auth_state.model.AuthStateEntity
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.presentation.IntentHandler
import kg.optima.mobile.core.error.Failure
import org.koin.core.component.inject

class AuthStateIntentHandler(
	override val stateMachine: AuthStatusStateMachine,
) : IntentHandler<AuthStateIntentHandler.AuthStateIntent, AuthStateEntity>() {

	private val clientInfoUseCase: ClientInfoUseCase by inject()

	override fun dispatch(intent: AuthStateIntent) {
		val operation: suspend () -> Either<Failure, AuthStateEntity> = {
			when (intent) {
				AuthStateIntent.CheckIsAuthorized -> clientInfoUseCase.execute(ClientInfoUseCase.Params)
			}
		}
		launchOperation(operation)
	}

	sealed interface AuthStateIntent : Intent {
		object CheckIsAuthorized : AuthStateIntent
	}
}
