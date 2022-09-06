package kg.optima.mobile.navigation

import kg.optima.mobile.feature.main.MainScreenModel

class NavigationComponent {
	val mainStack = mutableListOf<MainScreenModel>()
	val transfersStack = mutableListOf<MainScreenModel>()
	val paymentsStack = mutableListOf<MainScreenModel>()
	val historyStack = mutableListOf<MainScreenModel>()
	val menuStack = mutableListOf<MainScreenModel>()
}