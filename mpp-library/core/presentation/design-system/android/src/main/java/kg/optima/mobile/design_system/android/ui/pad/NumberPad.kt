package kg.optima.mobile.design_system.android.ui.pad

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.ComposeColors

@Composable
fun NumberPad() {

}

@Composable
private fun NumberRow(
	row: List<String>,
	onClick: (String) -> Unit
) {
	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.Center,
	) {
		repeat(row.size) { i ->
			val num = row[i]
			NumberCell(
				text = num,
				modifier = Modifier.padding(horizontal = Deps.Spacing.pinBtnXMargin),
				onClick = { onClick(num) }
			)
		}
	}
}

@Composable
private fun NumberCell(
	text: String,
	modifier: Modifier = Modifier,
	onClick: () -> Unit,
) {
	Button(
		modifier = modifier
			.size(Deps.Size.pinBtnSize)
			.padding(1.dp),
		shape = CircleShape,
		elevation = ButtonDefaults.elevation(defaultElevation = 4.dp),
		onClick = onClick,
		colors = ButtonDefaults.buttonColors(backgroundColor = ComposeColors.PrimaryWhite),
		content = {
			Box(modifier = Modifier.background(ComposeColors.PrimaryWhite)) {
				Text(
					text = text,
					modifier = Modifier.align(Alignment.Center),
					fontSize = Deps.TextSize.pinBtnText,
				)
			}
		}
	)
}