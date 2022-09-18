package kg.optima.mobile.feature.menu

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.core.navigation.ScreenModel

sealed interface MenuScreenModel : ScreenModel {
	@Parcelize
	object Main : MenuScreenModel
}