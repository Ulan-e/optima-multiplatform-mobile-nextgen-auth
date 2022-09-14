package kg.optima.mobile.feature.transfers

import kg.optima.mobile.core.navigation.ScreenModel

sealed interface TransfersScreenModel : ScreenModel {
	object Main : TransfersScreenModel
}