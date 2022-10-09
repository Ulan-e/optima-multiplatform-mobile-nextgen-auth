package kg.optima.mobile.registration.presentation.self_confirm

import kg.optima.mobile.base.presentation.UiIntent
import kg.optima.mobile.registration.presentation.self_confirm.model.*
import kg.optima.mobile.registration.presentation.self_confirm.model.items

class SelfConfirmIntent(
	override val uiState: SelfConfirmState,
) : UiIntent<SelfConfirmEntity>() {

    fun onPermissionsAccepted() = uiState.handle(SelfConfirmEntity.PermissionsAccepted)

    fun fadeAnimationModels(mode: IdentificationMode) = when (mode) {
        IdentificationMode.SHORT -> uiState.handle(SelfConfirmEntity.AnimationModels(itemsShort))
        IdentificationMode.FULL -> uiState.handle(SelfConfirmEntity.AnimationModels(items))
    }
}