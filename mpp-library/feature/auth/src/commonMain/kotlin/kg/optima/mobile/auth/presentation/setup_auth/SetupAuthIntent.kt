package kg.optima.mobile.auth.presentation.setup_auth

import kg.optima.mobile.auth.domain.usecase.biometry_auth.SetupBiometryUseCase
import kg.optima.mobile.auth.domain.usecase.pin_set.PinSetUseCase
import kg.optima.mobile.auth.domain.usecase.pin_set.SetupAuthResult
import kg.optima.mobile.base.presentation.UiIntent
import org.koin.core.component.inject

class SetupAuthIntent(
	override val uiState: SetupAuthState,
) : UiIntent<SetupAuthResult>() {

	private val pinSetUseCase: PinSetUseCase by inject()
	private val setupBiometryUseCase: SetupBiometryUseCase by inject()

	fun savePin(pin: String) {
		launchOperation {
			pinSetUseCase.execute(PinSetUseCase.Params.Save(pin))
		}
	}

	fun comparePin(pin: String) {
		launchOperation {
			pinSetUseCase.execute(PinSetUseCase.Params.Compare(pin))
		}
	}

	fun setBiometry(accessed: Boolean) {
		launchOperation {
			setupBiometryUseCase.execute(SetupBiometryUseCase.Params(accessed))
		}
	}
}
