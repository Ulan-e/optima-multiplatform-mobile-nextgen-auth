package kg.optima.mobile.design_system.android.ui.input.filter

import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

class NoTextFieldFilter : ITextFieldComposeFilter {
	override val validator: (String, Int) -> Boolean
		get() = { _, _ -> true }

	override val filter: (String, String, Boolean,) -> String
		get() = { _, cur, _, -> cur }

	override fun validData(oldText: String, newText: String): String {
		return filter(oldText, newText, validator(newText, maxLength))
	}

	/**
	 * Example
	 **/
	@Composable
	private fun SimpleFilledTextFieldSample(
		valueState: MutableState<String>,
		validators: List<ITextFieldComposeFilter> = listOf(NoTextFieldFilter()),
	) {
		TextField(
			value = valueState.value,
			onValueChange = { text ->
				val isValid =
					validators.isEmpty() || validators.all { it.isValid(valueState.value, text) }
				valueState.value = if (isValid) validators.first()
					.validData(valueState.value, text) else valueState.value
			}
		)
	}
}