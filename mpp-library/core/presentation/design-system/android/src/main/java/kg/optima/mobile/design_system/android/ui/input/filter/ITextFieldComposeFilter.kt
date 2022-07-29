package kg.optima.mobile.design_system.android.ui.input.filter


interface ITextFieldComposeFilter {

	val maxLength: Int
		get() = Int.MAX_VALUE

	val validator: (String, Int) -> Boolean
	val filter: (String, String, Boolean) -> String

	fun validData(oldText: String, newText: String): String

	fun isValid(oldText: String, newText: String): Boolean {
		return oldText == validData(oldText, newText)
	}
}
