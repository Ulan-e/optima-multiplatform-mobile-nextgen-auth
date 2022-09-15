package kg.optima.mobile.feature.main

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.core.navigation.ScreenModel

@Parcelize
object BottomNavScreenModel : ScreenModel {
	override val dropBackStack: Boolean = true
}