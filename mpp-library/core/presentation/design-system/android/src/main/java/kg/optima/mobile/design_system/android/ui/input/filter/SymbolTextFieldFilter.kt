package kg.optima.mobile.design_system.android.ui.input.filter


class SymbolTextFieldFilter : ITextFieldComposeFilter {

	private val symbols: List<String> = listOf("1", "2", "3")

	override val validator: (String, Int) -> Boolean
		get() = { text, _ ->
			if (text.isBlank()) true else symbols.contains(text.takeLast(1))
		}

	override val filter: (String, String, Boolean,) -> String
		get() = { prev, cur, isValid, ->
			if (isValid) cur else prev
		}

	override fun validData(oldText: String, newText: String): String {
		return filter(oldText, newText, validator(newText, maxLength))
	}

}