package kg.optima.mobile.auth.presentation.pin_enter

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.auth.presentation.AuthNavigateModel
import kg.optima.mobile.auth.presentation.login.LoginState
import kg.optima.mobile.auth.presentation.login.model.LoginEntity

open class PinEnterState : LoginState<PinEnterEntity>() {

	override fun handle(entity: PinEnterEntity) {
		super.handle(entity)
		val stateModel: Model = when (entity) {
			PinEnterEntity.Logout -> Model.NavigateTo.Main
		}

		setStateModel(stateModel)
	}

	sealed interface Model : LoginState.Model {
		sealed interface NavigateTo : Model, AuthNavigateModel {
			@Parcelize
			object Main : NavigateTo
		}
	}
}