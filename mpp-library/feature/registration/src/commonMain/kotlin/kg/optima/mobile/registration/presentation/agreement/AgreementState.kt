package kg.optima.mobile.registration.presentation.agreement

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.base.presentation.UiState

class AgreementState : UiState<AgreementModel>() {

	override fun handle(entity: AgreementModel) {
		val stateModel = when (entity) {
			is AgreementModel.AgreementInfo ->
				if (entity.confirmed) Model.NavigateTo.RegistrationEnterPhone else UiState.Model.Pop
			is AgreementModel.Offerta ->
				Model.NavigateTo.RegistrationOfferta(entity.url)
		}

		setStateModel(stateModel)
	}

	sealed interface Model : UiState.Model {
		sealed interface NavigateTo : Model, UiState.Model.Navigate {
			@Parcelize
			object RegistrationEnterPhone : NavigateTo
			@Parcelize
			class RegistrationOfferta(val url: String) : NavigateTo
		}
	}
}