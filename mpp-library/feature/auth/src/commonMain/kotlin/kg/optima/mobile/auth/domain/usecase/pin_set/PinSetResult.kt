package kg.optima.mobile.auth.domain.usecase.pin_set

sealed interface PinSetResult {
	object Save : PinSetResult

	class Compare(val result: Boolean) : PinSetResult
}