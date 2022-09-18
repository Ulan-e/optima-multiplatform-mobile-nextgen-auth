package kg.optima.mobile.feature.main

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.core.navigation.ScreenModel

sealed interface MainScreenModel : ScreenModel {
	@Parcelize
	object Main : MainScreenModel
}