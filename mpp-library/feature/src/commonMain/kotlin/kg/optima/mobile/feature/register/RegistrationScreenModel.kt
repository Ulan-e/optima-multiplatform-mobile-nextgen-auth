package kg.optima.mobile.feature.register

import kg.optima.mobile.core.navigation.ScreenModel

sealed interface RegistrationScreenModel : ScreenModel {
	object Agreement : RegistrationScreenModel
}