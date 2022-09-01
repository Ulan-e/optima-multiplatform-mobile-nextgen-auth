package kg.optima.mobile.feature.main

import kg.optima.mobile.core.navigation.ScreenModel

sealed interface MainScreenModel : ScreenModel {
	object Main : MainScreenModel {
		override val dropBackStack: Boolean = true
	}
}