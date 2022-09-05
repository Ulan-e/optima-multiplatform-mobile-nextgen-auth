package kg.optima.mobile.common.presentation.launch

import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfo
import kg.optima.mobile.auth.domain.usecase.client_info.ClientInfoUseCase
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.presentation.IntentHandler
import kg.optima.mobile.core.error.Failure
import org.koin.core.component.inject

class LaunchIntentHandler(
	override val stateMachine: LaunchStateMachine,
) : IntentHandler<LaunchIntentHandler.LaunchIntent, ClientInfo>() {

	private val clientInfoUseCase: ClientInfoUseCase by inject()

	override fun dispatch(intent: LaunchIntent) {
		val operation: suspend () -> Either<Failure, ClientInfo> = {
			when (intent) {
				is LaunchIntent.CheckIsAuthorized ->
					clientInfoUseCase.execute(ClientInfoUseCase.Params)
			}
		}
		launchOperation(operation)
	}

	sealed interface LaunchIntent : Intent {
		object CheckIsAuthorized : LaunchIntent
	}
}
