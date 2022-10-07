package kg.optima.mobile.registration.presentation.interview

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.base.presentation.UiState

class InterviewState : UiState<InterviewEntity>() {

	override fun handle(entity: InterviewEntity) {
		setStateModel(Model.NavigateToMain)
	}

	sealed interface Model : UiState.Model {
		@Parcelize
		object NavigateToMain : Model, UiState.Model.Navigate
	}
}