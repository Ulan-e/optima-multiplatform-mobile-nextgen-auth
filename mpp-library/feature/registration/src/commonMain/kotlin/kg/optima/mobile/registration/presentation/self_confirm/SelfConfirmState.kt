package kg.optima.mobile.registration.presentation.self_confirm

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.registration.presentation.RegistrationNavigateModel
import kg.optima.mobile.registration.presentation.self_confirm.model.AnimationModel

class SelfConfirmState : UiState<SelfConfirmEntity>() {
    override fun handle(entity: SelfConfirmEntity) {
        val stateModel = when (entity) {
            is SelfConfirmEntity.AnimationModels ->
                Model.AnimationModels(entity.models)
            SelfConfirmEntity.PermissionsAccepted ->
                Model.NavigateTo.DocumentScan
        }

        setStateModel(stateModel)
    }

    sealed interface Model : UiState.Model {
        class AnimationModels(
            val models: List<AnimationModel>,
        ) : Model

        sealed interface NavigateTo : Model, RegistrationNavigateModel {
            @Parcelize
            object DocumentScan : NavigateTo, RegistrationNavigateModel
        }
    }
}