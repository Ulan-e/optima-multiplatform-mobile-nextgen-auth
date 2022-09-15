package kg.optima.mobile.feature.transfers

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.core.navigation.ScreenModel

sealed interface TransfersScreenModel : ScreenModel {
	@Parcelize
	object Main : TransfersScreenModel
}