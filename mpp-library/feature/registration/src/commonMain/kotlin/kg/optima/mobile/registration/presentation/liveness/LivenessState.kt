package kg.optima.mobile.registration.presentation.liveness

import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.feature.register.RegistrationScreenModel

class LivenessState : State<LivenessInfo>() {

    override fun handle(entity: LivenessInfo) {
        if (entity.passed) {
            setStateModel(StateModel.Navigate(RegistrationScreenModel.ControlQuestion))
        } else {
            setStateModel(StateModel.Pop)
        }
    }
}