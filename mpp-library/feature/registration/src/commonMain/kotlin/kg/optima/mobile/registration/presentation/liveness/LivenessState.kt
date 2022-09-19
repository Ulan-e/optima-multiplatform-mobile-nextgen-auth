package kg.optima.mobile.registration.presentation.liveness

import kg.optima.mobile.base.presentation.State

class LivenessState : State<LivenessInfo>() {

    override fun handle(entity: LivenessInfo) {
        val stateModel: StateModel = if (entity.passed) {
            LivenessModel.Passed(entity.message)
        } else {
            LivenessModel.Failed(entity.message)
        }
        setStateModel(stateModel)
    }

    sealed interface LivenessModel : StateModel {
        val message: String

        class Passed(
            override val message: String
        ) : LivenessModel

        class Failed(
            override val message: String
        ) : LivenessModel
    }
}