package kg.optima.mobile.design_system.android.ui.containers

sealed interface Status {
	object Initial : Status
	object Loading : Status
}