package kg.optima.mobile.registration.presentation.liveness

import kg.optima.mobile.base.presentation.BaseMppState
import kg.optima.mobile.core.navigation.ScreenModel

class LivenessState : BaseMppState<LivenessState.LivenessModel>() {

    override fun handle(entity: LivenessModel) {
        val stateModel: StateModel = when (entity) {
            is LivenessModel.Passed -> LivenessModel.Passed(entity.passed, entity.message)
            is LivenessModel.NextScreen -> StateModel.Navigate(entity.screenModel)
        }
        setStateModel(stateModel)
    }

    sealed interface LivenessModel : StateModel {
        class Passed(
            val passed: Boolean,
            val message: String?
        ) : LivenessModel

        class NextScreen(
            val screenModel: ScreenModel
        ) : LivenessModel
    }
}