package kg.optima.mobile.auth.presentation.pin_enter

import kg.optima.mobile.auth.presentation.login.model.LoginEntity

sealed interface PinEnterEntity : LoginEntity {
	object Logout : PinEnterEntity

	sealed interface NavigateTo : PinEnterEntity {
		object Login: PinEnterEntity
	}
}