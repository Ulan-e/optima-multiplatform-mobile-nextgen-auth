package kg.optima.mobile.auth.presentation.setup_auth

import kg.optima.mobile.auth.domain.usecase.biometry_auth.SetupBiometryUseCase
import kg.optima.mobile.auth.domain.usecase.pin_set.SetupAuthResult
import kg.optima.mobile.auth.domain.usecase.pin_set.PinSetUseCase
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.presentation.IntentHandler
import kg.optima.mobile.core.error.Failure
import org.koin.core.component.inject

class SetupAuthIntentHandler(
	override val stateMachine: SetupAuthStateMachine,
) : IntentHandler<SetupAuthIntentHandler.SetupAuthIntent, SetupAuthResult>() {

	private val pinSetUseCase: PinSetUseCase by inject()
	private val setupBiometryUseCase: SetupBiometryUseCase by inject()

	override fun dispatch(intent: SetupAuthIntent) {
		val operation: suspend () -> Either<Failure, SetupAuthResult> = {
			when (intent) {
				is SetupAuthIntent.SavePin -> pinSetUseCase.execute(PinSetUseCase.Params.Save(intent.pin))
				is SetupAuthIntent.ComparePin -> pinSetUseCase.execute(PinSetUseCase.Params.Compare(intent.pin))
				is SetupAuthIntent.Biometry -> setupBiometryUseCase.execute(SetupBiometryUseCase.Params(intent.accessed))
			}
		}

		launchOperation(operation = operation)
	}

	sealed interface SetupAuthIntent : Intent {
		class SavePin(
			val pin: String,
		) : SetupAuthIntent

		class ComparePin(
			val pin: String,
		) : SetupAuthIntent

		class Biometry(
			val accessed: Boolean,
		) : SetupAuthIntent
	}
}
