package kg.optima.mobile.auth.presentation.setup_auth

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.auth.domain.usecase.pin_set.SetupAuthResult
import kg.optima.mobile.base.presentation.UiState

class SetupAuthState : UiState<SetupAuthResult>() {

	override fun handle(entity: SetupAuthResult) {
		val state = when (entity) {
			SetupAuthResult.Save -> SetupAuthStateModel.SavePin
			is SetupAuthResult.Compare -> SetupAuthStateModel.ComparePin(entity.isMatch)
			SetupAuthResult.Done -> SetupAuthStateModel.NavigateToMain
		}

		setStateModel(state)
	}

	sealed interface SetupAuthStateModel : Model {
		object SavePin : SetupAuthStateModel
		class ComparePin(val isMatches: Boolean) : SetupAuthStateModel

		@Parcelize
		object NavigateToMain : SetupAuthStateModel, Model.Navigate
	}
}