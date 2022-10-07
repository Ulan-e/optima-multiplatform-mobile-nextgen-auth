package kg.optima.mobile.registration.presentation.liveness

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.base.presentation.UiState

class LivenessState : UiState<LivenessEntity>() {

	override fun handle(entity: LivenessEntity) {
		val stateModel: Model = when (entity) {
			is LivenessEntity.Passed -> LivenessModel.Passed(entity.passed, entity.message)
			LivenessEntity.NavigateTo.Contacts -> LivenessModel.NavigateTo.Contacts
			LivenessEntity.NavigateTo.ControlQuestion -> LivenessModel.NavigateTo.Contacts
			LivenessEntity.NavigateTo.SelfConfirm -> LivenessModel.NavigateTo.Contacts
			LivenessEntity.NavigateTo.Welcome -> LivenessModel.NavigateTo.Contacts
		}
		setStateModel(stateModel)
	}

	sealed interface LivenessModel : Model {
		class Passed(
			val passed: Boolean,
			val message: String?
		) : LivenessModel

		sealed interface NavigateTo : LivenessModel, Model.Navigate {
			@Parcelize
			object Contacts : NavigateTo

			@Parcelize
			object Welcome : NavigateTo

			@Parcelize
			object ControlQuestion : NavigateTo

			@Parcelize
			object SelfConfirm : NavigateTo
		}
	}
}