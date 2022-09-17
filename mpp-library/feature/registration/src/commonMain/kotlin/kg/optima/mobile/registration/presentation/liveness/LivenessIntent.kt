package kg.optima.mobile.registration.presentation.liveness

import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.data.model.onSuccess
import kg.optima.mobile.base.presentation.Intent
import kg.optima.mobile.registration.domain.usecase.VerifyClientUseCase
import org.koin.core.component.inject

class LivenessIntent(
    override val state: LivenessState
) : Intent<LivenessInfo>() {

    private val verifyClientUseCase: VerifyClientUseCase by inject()

    fun verify(sessionId: String, livenessResult: String) {
        launchOperation {
            verifyClientUseCase.execute(
                model = VerifyClientUseCase.Params(
                    sessionId = sessionId,
                    livenessResult = livenessResult
                )
            ).map {
                LivenessInfo(passed = true)
            }.onSuccess { livenessInfo ->
                state.handle(livenessInfo)
            }
        }
    }
}