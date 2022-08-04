package kg.optima.mobile.auth.presentation.pin_set

import kg.optima.mobile.auth.domain.usecase.pin_set.PinSetResult
import kg.optima.mobile.auth.domain.usecase.pin_set.PinSetUseCase
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.presentation.IntentHandler
import kg.optima.mobile.core.error.Failure
import org.koin.core.component.inject

class PinSetIntentHandler(
	override val stateMachine: PinSetStateMachine,
) : IntentHandler<PinSetIntentHandler.PinSetIntent, PinSetResult>() {

	private val pinSetUseCase: PinSetUseCase by inject()

	override fun dispatch(intent: PinSetIntent) {
		val operation: suspend () -> Either<Failure, PinSetResult> = {
			when (intent) {
				is PinSetIntent.Save -> pinSetUseCase.execute(PinSetUseCase.Params.Save(intent.pin))
				is PinSetIntent.Compare -> pinSetUseCase.execute(PinSetUseCase.Params.Compare(intent.pin))
			}
		}

		launchOperation(operation = operation)
	}

	sealed interface PinSetIntent : Intent {
		class Save(
			val pin: String,
		) : PinSetIntent

		class Compare(
			val pin: String,
		) : PinSetIntent
	}
}
