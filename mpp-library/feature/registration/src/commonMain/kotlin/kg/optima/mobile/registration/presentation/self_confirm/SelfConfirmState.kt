package kg.optima.mobile.registration.presentation.self_confirm

import kg.optima.mobile.base.presentation.BaseMppState
import kg.optima.mobile.feature.registration.RegistrationScreenModel
import kg.optima.mobile.registration.presentation.self_confirm.model.AnimationModel

class SelfConfirmState : BaseMppState<SelfConfirmModel>() {
    override fun handle(entity: SelfConfirmModel) {
        val stateModel = when (entity) {
            is SelfConfirmModel.AnimationModels ->
                SelfConfirmStateModel.AnimationModels(entity.models)
            SelfConfirmModel.NextScreen -> {
                val nextScreenModel = RegistrationScreenModel.ControlQuestion
                StateModel.Navigate(nextScreenModel)
            }
        }

        setStateModel(stateModel)
    }

    sealed interface SelfConfirmStateModel : StateModel {
        class AnimationModels(
            val models: List<AnimationModel>
        ) : SelfConfirmStateModel
    }
}