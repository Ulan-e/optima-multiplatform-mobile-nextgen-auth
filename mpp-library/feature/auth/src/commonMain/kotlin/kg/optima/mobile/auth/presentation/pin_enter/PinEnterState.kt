package kg.optima.mobile.auth.presentation.pin_enter

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.auth.presentation.AuthNavigateModel
import kg.optima.mobile.auth.presentation.login.LoginState
import kg.optima.mobile.auth.presentation.login.model.LoginEntity

class PinEnterState : LoginState() {

	override fun handle(entity: LoginEntity) {
		when (entity) {
			is PinEnterEntity -> {
				val stateModel: Model = when (entity) {
					PinEnterEntity.Logout -> Model.NavigateTo.Welcome
				}

				setStateModel(stateModel)
			}
			else -> {
				if (entity is LoginEntity.ClientInfo) // TODO now working
					setStateModel(Model.Biometry(entity.biometryEnabled))
				super.handle(entity)
			}
		}
	}

	sealed interface Model : LoginState.Model {
		class Biometry(val enabled: Boolean) : Model

		sealed interface NavigateTo : Model, AuthNavigateModel {
			@Parcelize
			object Welcome : NavigateTo
		}
	}
}