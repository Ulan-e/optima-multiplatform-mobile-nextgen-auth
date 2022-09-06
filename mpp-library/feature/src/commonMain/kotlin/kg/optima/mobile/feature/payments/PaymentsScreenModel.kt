package kg.optima.mobile.feature.payments

import kg.optima.mobile.core.navigation.ScreenModel

sealed interface PaymentsScreenModel : ScreenModel {
	object Main : PaymentsScreenModel
}