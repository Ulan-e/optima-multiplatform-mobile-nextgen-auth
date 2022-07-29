package kg.optima.mobile.design_system.android.ui.input.filter


class FloatTextFieldFilter : ITextFieldComposeFilter {
	override val validator: (String, Int) -> Boolean
		get() = { text, _ ->
			try {
				text.toFloat(); true
			} catch (e: Exception) {
				text.isBlank()
			}
		}

	override val filter: (String, String, Boolean,) -> String
		get() = { prev, cur, isValid, ->
			if (isValid) cur else prev
		}

	override fun validData(oldText: String, newText: String): String {
		return filter(oldText, newText, validator(newText, maxLength))
	}

}