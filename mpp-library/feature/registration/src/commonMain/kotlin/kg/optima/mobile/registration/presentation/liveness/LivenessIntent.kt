package kg.optima.mobile.registration.presentation.liveness

import kg.optima.mobile.base.presentation.Intent
import kg.optima.mobile.base.presentation.State

class LivenessIntent(
    override val state: State<LivenessInfo>
) : Intent<LivenessInfo>() {

    fun confirm() {
        state.handle(LivenessInfo(true))
    }
}