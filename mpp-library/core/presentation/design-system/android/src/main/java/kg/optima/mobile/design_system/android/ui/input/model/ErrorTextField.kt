package kg.optima.mobile.design_system.android.ui.input.model

data class ErrorTextField(
    val message: String,
    val isError: Boolean,
) {
    companion object {
        fun empty() = ErrorTextField(
            message = "",
            isError = false
        )
    }
}