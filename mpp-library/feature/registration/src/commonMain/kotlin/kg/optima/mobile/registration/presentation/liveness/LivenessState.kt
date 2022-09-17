package kg.optima.mobile.registration.presentation.liveness

import kg.optima.mobile.base.presentation.State

class LivenessState : State<LivenessInfo>() {

    override fun handle(entity: LivenessInfo) {
        if (entity.passed) {
            setStateModel(StateModel.Error.BaseError(entity.message))
        } else {
            setStateModel(StateModel.Pop)
        }
    }
}