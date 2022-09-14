package kg.optima.mobile.feature.welcome

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.core.navigation.ScreenModel

sealed interface WelcomeScreenModel : ScreenModel {
	@Parcelize
	object Welcome : WelcomeScreenModel
}