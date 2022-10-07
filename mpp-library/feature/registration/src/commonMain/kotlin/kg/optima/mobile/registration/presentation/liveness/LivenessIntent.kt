package kg.optima.mobile.registration.presentation.liveness

import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.UiIntent
import kg.optima.mobile.registration.domain.usecase.VerifyClientUseCase
import org.koin.core.component.inject

class LivenessIntent(
	override val uiState: LivenessState
) : UiIntent<LivenessEntity>() {

	private val verifyClientUseCase: VerifyClientUseCase by inject()

	fun verify(sessionId: String, livenessResult: String, data: Map<String, String>) {
		launchOperation {
			verifyClientUseCase.execute(
				model = VerifyClientUseCase.Params(
					sessionId = sessionId,
					livenessResult = livenessResult,
					data = data
				)
			).map {
                LivenessEntity.Passed(passed = it.success, message = it.message)
			}
		}
	}

    fun navigateToContacts() =
        uiState.handle(LivenessEntity.NavigateTo.Contacts)

    fun navigateToWelcome() =
        uiState.handle(LivenessEntity.NavigateTo.Welcome)

    fun navigateToControlQuestion() =
        uiState.handle(LivenessEntity.NavigateTo.ControlQuestion)

    fun navigateToSelfConfirm() =
        uiState.handle(LivenessEntity.NavigateTo.SelfConfirm)

}