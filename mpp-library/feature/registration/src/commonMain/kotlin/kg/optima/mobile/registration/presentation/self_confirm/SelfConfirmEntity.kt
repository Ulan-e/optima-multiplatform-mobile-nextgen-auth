package kg.optima.mobile.registration.presentation.self_confirm

import kg.optima.mobile.base.presentation.BaseEntity
import kg.optima.mobile.registration.presentation.self_confirm.model.AnimationModel

sealed interface SelfConfirmEntity : BaseEntity {
    object PermissionsAccepted : SelfConfirmEntity

    class AnimationModels(
        val models: List<AnimationModel>
    ) : SelfConfirmEntity
}