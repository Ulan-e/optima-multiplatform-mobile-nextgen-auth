package kg.optima.mobile.design_system.android.ui.toggles

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kg.optima.mobile.resources.ComposeColors

@Composable
fun Toggle(
	checkedTrackColor: Color = ComposeColors.secondarySystemGreen,
	onClick: (Boolean) -> Unit = {}
) {
	val interactionSource = remember { MutableInteractionSource() }
	var checked by remember { mutableStateOf(true) }
	val alignment by animateAlignmentAsState(if (checked) 1f else -1f)

	Box(
		modifier = Modifier
			.size(width = 64.dp, height = 40.dp)
			.background(
				color = if (checked) checkedTrackColor else Color.Gray,
				shape = RoundedCornerShape(percent = 50)
			)
			.clickable(
				indication = null,
				interactionSource = interactionSource,
				onClick = { checked = !checked; onClick(checked) }
			),
		contentAlignment = Alignment.Center
	) {
		Box(
			modifier = Modifier
				.padding(start = 4.dp, end = 4.dp)
				.fillMaxSize(),
			contentAlignment = alignment
		) {
			Icon(
				imageVector = Icons.Default.Done,
				contentDescription = if (checked) "Enbld" else "Dsbld",
				modifier = Modifier
					.size(size = 30.dp)
					.background(color = Color.White, shape = CircleShape),
				tint = Color.White
			)
		}
	}
}

@Composable
private fun animateAlignmentAsState(
	targetBiasValue: Float
): State<BiasAlignment> {
	val bias by animateFloatAsState(targetBiasValue)
	return derivedStateOf { BiasAlignment(horizontalBias = bias, verticalBias = 0f) }
}