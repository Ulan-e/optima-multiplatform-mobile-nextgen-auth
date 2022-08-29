package kg.optima.mobile.feature.welcome

import kg.optima.mobile.core.navigation.ScreenModel

sealed interface WelcomeScreenModel : ScreenModel {
	object Welcome : WelcomeScreenModel
}