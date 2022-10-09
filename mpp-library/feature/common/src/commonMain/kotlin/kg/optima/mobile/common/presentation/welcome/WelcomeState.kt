package kg.optima.mobile.common.presentation.welcome

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.common.presentation.welcome.model.WelcomeEntity

class WelcomeState : UiState<WelcomeEntity>() {

	override fun handle(entity: WelcomeEntity) {
		val stateModel = when (entity) {
			WelcomeEntity.Login -> Model.NavigateTo.Login
			WelcomeEntity.Register -> Model.NavigateTo.RegisterAgreement
			is WelcomeEntity.ButtonBlock -> when (entity) {
				WelcomeEntity.ButtonBlock.Map -> Model.NavigateTo.Map
				WelcomeEntity.ButtonBlock.Languages -> Model.NavigateTo.Languages
				WelcomeEntity.ButtonBlock.Rates -> Model.NavigateTo.Rates
				WelcomeEntity.ButtonBlock.Contacts -> Model.NavigateTo.Contacts
			}
		}

		setStateModel(stateModel)
	}

	sealed interface Model : UiState.Model {
		sealed interface NavigateTo : Model, UiState.Model.Navigate {
			@Parcelize
			object Login : NavigateTo
			@Parcelize
			object RegisterAgreement : NavigateTo
			@Parcelize
			object Map : NavigateTo
			@Parcelize
			object Languages : NavigateTo
			@Parcelize
			object Rates : NavigateTo
			@Parcelize
			object Contacts : NavigateTo
		}
	}
}