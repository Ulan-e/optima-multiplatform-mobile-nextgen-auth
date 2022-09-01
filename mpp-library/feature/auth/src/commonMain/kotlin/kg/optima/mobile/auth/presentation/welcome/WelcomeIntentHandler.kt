package kg.optima.mobile.auth.presentation.welcome

import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfoUseCase
import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfo
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.presentation.IntentHandler
import kg.optima.mobile.core.error.Failure
import org.koin.core.component.inject

class WelcomeIntentHandler(
	override val stateMachine: WelcomeStateMachine,
) : IntentHandler<WelcomeIntentHandler.WelcomeIntent, ClientInfo>() {

	private val clientInfoUseCase: ClientInfoUseCase by inject()

	override fun dispatch(intent: WelcomeIntent) {
		val operation: suspend () -> Either<Failure, ClientInfo> = {
			when (intent) {
				is WelcomeIntent.CheckIsAuthorized ->
					clientInfoUseCase.execute(ClientInfoUseCase.Params)
			}
		}
		launchOperation(operation)
	}

	sealed interface WelcomeIntent : Intent {
		object CheckIsAuthorized : WelcomeIntent
	}
}
