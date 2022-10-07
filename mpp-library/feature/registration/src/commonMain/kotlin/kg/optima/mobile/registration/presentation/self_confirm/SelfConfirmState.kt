package kg.optima.mobile.registration.presentation.self_confirm

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.registration.presentation.self_confirm.model.AnimationModel

class SelfConfirmState : UiState<SelfConfirmEntity>() {
    override fun handle(entity: SelfConfirmEntity) {
        val stateModel = when (entity) {
            is SelfConfirmEntity.AnimationModels ->
                SelfConfirmStateModel.AnimationModels(entity.models)
            SelfConfirmEntity.PermissionsAccepted ->
                SelfConfirmStateModel.NavigateToDocumentScan
        }

        setStateModel(stateModel)
    }

    sealed interface SelfConfirmStateModel : Model {
        class AnimationModels(
            val models: List<AnimationModel>
        ) : SelfConfirmStateModel

        @Parcelize
        object NavigateToDocumentScan : SelfConfirmStateModel, Model.Navigate
    }
}