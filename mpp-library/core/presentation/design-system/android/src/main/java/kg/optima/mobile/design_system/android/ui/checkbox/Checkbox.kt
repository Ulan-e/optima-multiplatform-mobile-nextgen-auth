package kg.optima.mobile.design_system.android.ui.checkbox

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.dp
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.Headings.Companion.px

@Composable
fun Checkbox(
	modifier: Modifier = Modifier,
	checkedState: MutableState<Boolean>,
	text: String = "",
	onCheck: (Boolean) -> Unit = {}
) {
	val checkboxColor = ComposeColors.Green

	Row(
		modifier = modifier
	) {
		Card(
			modifier = Modifier.background(ComposeColors.PrimaryWhite),
			elevation = 0.dp,
			shape = RoundedCornerShape(size = Deps.checkboxCornerRadius),
			border = BorderStroke(
				width = 2.dp,
				color = if (checkedState.value) {
					checkboxColor
				} else {
					ComposeColors.DescriptionGray
				}
			)
		) {
			Box(
				modifier = Modifier
					.size(Deps.Size.checkboxSize)
					.background(ComposeColors.PrimaryWhite)
					.clickable {
						checkedState.value = !checkedState.value
						onCheck(checkedState.value)
					},
				contentAlignment = Alignment.Center
			) {
				if (checkedState.value) {
					Icon(
						modifier = Modifier.padding(1.dp),
						imageVector = Icons.Default.Check,
						contentDescription = null,
						tint = checkboxColor
					)
				}
			}
		}
		Text(
			modifier = Modifier
				.align(Alignment.CenterVertically)
				.padding(start = 8.dp)
				.clickable {
					checkedState.value = !checkedState.value
					onCheck(checkedState.value)
				},
			text = text,
			color = ComposeColors.DescriptionGray,
			fontSize = Headings.H4.px().dp().sp(),
		)
	}
}