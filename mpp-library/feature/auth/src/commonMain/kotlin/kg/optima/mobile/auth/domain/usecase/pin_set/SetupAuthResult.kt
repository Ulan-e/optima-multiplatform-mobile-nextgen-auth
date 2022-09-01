package kg.optima.mobile.auth.domain.usecase.pin_set

sealed interface SetupAuthResult {
	object Save : SetupAuthResult

	class Compare(val isMatch: Boolean) : SetupAuthResult

	object Done : SetupAuthResult
}