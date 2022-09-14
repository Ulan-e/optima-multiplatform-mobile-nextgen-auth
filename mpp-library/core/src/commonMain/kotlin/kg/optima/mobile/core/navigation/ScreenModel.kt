package kg.optima.mobile.core.navigation

import com.arkivanov.essenty.parcelable.Parcelable


interface ScreenModel : Parcelable {
	val dropBackStack: Boolean get() = false
}
