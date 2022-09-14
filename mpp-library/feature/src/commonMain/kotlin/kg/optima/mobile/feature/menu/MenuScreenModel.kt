package kg.optima.mobile.feature.menu

import kg.optima.mobile.core.navigation.ScreenModel

sealed interface MenuScreenModel : ScreenModel {
	object Main : MenuScreenModel
}