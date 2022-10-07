package kg.optima.mobile.auth.domain.usecase.pin_set

import kg.optima.mobile.base.presentation.BaseEntity

sealed interface SetupAuthResult : BaseEntity {
	object Save : SetupAuthResult

	class Compare(val isMatch: Boolean) : SetupAuthResult

	object Done : SetupAuthResult
}