package kg.optima.mobile.feature.history

import kg.optima.mobile.core.navigation.ScreenModel

sealed interface HistoryScreenModel : ScreenModel {
	object Main : HistoryScreenModel
}