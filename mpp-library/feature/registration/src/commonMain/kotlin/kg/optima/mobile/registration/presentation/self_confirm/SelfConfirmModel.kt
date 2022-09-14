package kg.optima.mobile.registration.presentation.self_confirm

import kg.optima.mobile.registration.presentation.self_confirm.model.AnimationModel

sealed interface SelfConfirmModel {
    object NextScreen : SelfConfirmModel

    class AnimationModels(
        val models: List<AnimationModel>
    ) : SelfConfirmModel
}