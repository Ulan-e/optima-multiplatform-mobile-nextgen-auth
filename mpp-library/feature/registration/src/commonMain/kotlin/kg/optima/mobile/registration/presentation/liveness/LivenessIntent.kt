package kg.optima.mobile.registration.presentation.liveness

import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.data.model.onFailure
import kg.optima.mobile.base.data.model.onSuccess
import kg.optima.mobile.base.presentation.Intent
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.registration.domain.usecase.VerifyClientUseCase
import org.koin.core.component.inject

class LivenessIntent(
    override val state: LivenessState
) : Intent<LivenessState.LivenessModel>() {

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
                LivenessState.LivenessModel.Passed(passed = it.success, message = it.message)
            }
        }
    }

    fun navigate(screenModel: ScreenModel){
        state.handle(LivenessState.LivenessModel.NextScreen(screenModel))
    }
}