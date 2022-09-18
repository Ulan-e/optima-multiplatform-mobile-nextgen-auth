package kg.optima.mobile.feature.history

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.core.navigation.ScreenModel

sealed interface HistoryScreenModel : ScreenModel {
	@Parcelize
	object Main : HistoryScreenModel
}