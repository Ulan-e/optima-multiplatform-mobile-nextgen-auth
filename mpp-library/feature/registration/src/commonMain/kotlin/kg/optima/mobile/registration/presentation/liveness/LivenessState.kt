package kg.optima.mobile.registration.presentation.liveness

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.registration.presentation.RegistrationNavigateModel

class LivenessState : UiState<LivenessEntity>() {

	override fun handle(entity: LivenessEntity) {
		val stateModel: UiState.Model = when (entity) {
			is LivenessEntity.Passed -> Model.Passed(entity.passed, entity.message)
			LivenessEntity.NavigateTo.Contacts -> Model.NavigateTo.Contacts
			LivenessEntity.NavigateTo.ControlQuestion -> Model.NavigateTo.Contacts
			LivenessEntity.NavigateTo.SelfConfirm -> Model.NavigateTo.Contacts
			LivenessEntity.NavigateTo.Welcome -> Model.NavigateTo.Contacts
		}
		setStateModel(stateModel)
	}

	sealed interface Model : UiState.Model {
		class Passed(
			val passed: Boolean,
			val message: String?
		) : Model

		sealed interface NavigateTo : Model, RegistrationNavigateModel {
			@Parcelize
			object Contacts : NavigateTo

			@Parcelize
			object Welcome : NavigateTo

			@Parcelize
			class ControlQuestion(
				val hashCode: String
			) : NavigateTo

			@Parcelize
			object SelfConfirm : NavigateTo
		}
	}
}