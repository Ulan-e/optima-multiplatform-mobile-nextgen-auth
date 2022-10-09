package kg.optima.mobile.registration.presentation.interview

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.registration.presentation.RegistrationNavigateModel

class InterviewState : UiState<InterviewEntity>() {

	override fun handle(entity: InterviewEntity) {
		setStateModel(Model.NavigateTo.Main)
	}

	sealed interface Model : UiState.Model {
		sealed interface NavigateTo : Model, RegistrationNavigateModel {
			@Parcelize
			object Main : NavigateTo, RegistrationNavigateModel
		}
	}
}