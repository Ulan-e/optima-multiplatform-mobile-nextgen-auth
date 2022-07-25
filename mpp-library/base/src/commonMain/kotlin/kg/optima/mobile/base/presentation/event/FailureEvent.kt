package kg.optima.mobile.base.presentation.event

sealed interface FailureEvent {
    class NoInternet : FailureEvent
}