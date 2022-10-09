package kg.optima.mobile.auth.presentation.setup_auth

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.auth.domain.usecase.pin_set.SetupAuthResult
import kg.optima.mobile.auth.presentation.AuthNavigateModel
import kg.optima.mobile.base.presentation.UiState

class SetupAuthState : UiState<SetupAuthResult>() {

	override fun handle(entity: SetupAuthResult) {
		val state = when (entity) {
			SetupAuthResult.Save -> Model.SavePin
			is SetupAuthResult.Compare -> Model.ComparePin(entity.isMatch)
			SetupAuthResult.Done -> Model.NavigateTo.Main
			SetupAuthResult.Skip -> Model.NavigateTo.Main
		}

		setStateModel(state)
	}

	sealed interface Model : UiState.Model {
		object SavePin : Model
		class ComparePin(val isMatches: Boolean) : Model

		sealed interface NavigateTo : Model, AuthNavigateModel {
			@Parcelize
			object Main : NavigateTo, AuthNavigateModel
		}
	}
}