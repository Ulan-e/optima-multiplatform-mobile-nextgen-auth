package kg.optima.mobile.design_system.android.ui.input.filter


class LengthFieldFilter(override val maxLength: Int = 5) : ITextFieldComposeFilter {
	override val validator: (String, Int) -> Boolean
		get() = { _, _ -> false }

	override val filter: (String, String, Boolean,) -> String
		get() = { _, cur, _, -> cur }

	override fun validData(oldText: String, newText: String): String {
		return if (newText.length > maxLength) {
			newText.substring(0, maxLength)
		} else {
			newText
		}
	}
}