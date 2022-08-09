package kg.optima.mobile.design_system.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kg.optima.mobile.design_system.android.ui.input.CodeInput
import kg.optima.mobile.design_system.android.ui.pad.CellType
import kg.optima.mobile.design_system.android.ui.pad.NumberPad
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.ComposeColors
import kg.optima.mobile.resources.Headings

@Composable
fun PinScreen(
	headerState: MutableState<String>,
	subheaderState: MutableState<String>,
	codeState: MutableState<String>,
	onValueChanged: (String) -> Unit = {},
	onInputCompleted: (String) -> Unit = {},
) {
	Column(
		modifier = Modifier
			.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
	) {
		Spacer(modifier = Modifier.weight(1f))
		Text(
			text = headerState.value,
			fontSize = Headings.H1.px.sp,
			fontWeight = FontWeight.Bold,
			color = ComposeColors.PrimaryDisabledGray,
		)
		Text(
			text = subheaderState.value,
			modifier = Modifier.padding(top = Deps.Spacing.subheaderMargin),
			fontSize = Headings.H5.px.sp,
			color = ComposeColors.DescriptionGray,
		)
		Spacer(modifier = Modifier.weight(1f))
		CodeInput(
			value = codeState.value,
			onValueChanged = { codeState.value = it; onValueChanged(it) },
			onInputCompleted = onInputCompleted,
		)
		Spacer(modifier = Modifier.weight(1f))
		NumberPad(
			modifier = Modifier.padding(
				start = Deps.Spacing.numPadXMargin,
				end = Deps.Spacing.numPadXMargin,
			),
			onClick = { type ->
				when (type) {
					is CellType.Img -> {
						if (codeState.value.isNotBlank()) {
							val sliced =
								codeState.value.slice(0 until codeState.value.length - 1)
							codeState.value = sliced
						}
					}
					is CellType.Num -> {
						codeState.value = codeState.value + type.num
					}
					is CellType.Text -> Unit
				}
			}
		)
		Spacer(modifier = Modifier.weight(1f))
	}
}