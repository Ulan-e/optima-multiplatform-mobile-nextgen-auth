package kg.optima.mobile.registration.presentation.liveness

import kg.optima.mobile.base.presentation.State

class LivenessState : State<LivenessInfo>() {

    override fun handle(entity: LivenessInfo) {
        setStateModel(LivenessModel.Passed(entity.message))
    }

    sealed interface LivenessModel : StateModel {
        class Passed(
            val message: String
        ) : LivenessModel
    }
}