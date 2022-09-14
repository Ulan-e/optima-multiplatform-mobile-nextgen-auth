package kg.optima.mobile.design_system.android.ui.screens.pin

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kg.optima.mobile.design_system.android.ui.input.CodeInput
import kg.optima.mobile.design_system.android.ui.pad.Cell
import kg.optima.mobile.design_system.android.ui.pad.NumberPad
import kg.optima.mobile.design_system.android.values.Deps

@Composable
fun PinScreen(
	header: @Composable ColumnScope.(Modifier) -> Unit,
	codeState: MutableState<String>,
	onValueChanged: (String) -> Unit = {},
	onInputCompleted: (String) -> Unit = {},
	actionCell: ActionCell,
) {
	Column(
		modifier = Modifier.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
	) {
		header(Modifier.weight(2f))
		CodeInput(
			value = codeState.value,
			onValueChanged = { codeState.value = it; onValueChanged(it) },
			onInputCompleted = onInputCompleted,
		)
		Spacer(modifier = Modifier.weight(1f))
		NumberPad(
			modifier = Modifier
				.padding(horizontal = Deps.Spacing.numPadXMargin),
			onClick = { type ->
				when (type) {
					is Cell.Img -> {
						if (codeState.value.isNotBlank()) {
							val sliced =
								codeState.value.slice(0 until codeState.value.length - 1)
							codeState.value = sliced
						}
					}
					is Cell.Num -> {
						codeState.value = codeState.value + type.num
					}
					is Cell.Text -> type.onClick(type)
					else -> Unit
				}
			},
			actionCell = actionCell.cell,
		)
		Spacer(modifier = Modifier.weight(1f))
	}
}