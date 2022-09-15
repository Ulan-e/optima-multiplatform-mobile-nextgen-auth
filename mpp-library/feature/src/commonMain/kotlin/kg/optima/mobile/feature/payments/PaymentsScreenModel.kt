package kg.optima.mobile.feature.payments

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.core.navigation.ScreenModel

sealed interface PaymentsScreenModel : ScreenModel {
	@Parcelize
	object Main : PaymentsScreenModel
}