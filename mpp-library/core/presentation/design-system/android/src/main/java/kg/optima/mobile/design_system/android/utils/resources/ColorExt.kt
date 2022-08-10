package kg.optima.mobile.design_system.android.utils.resources

import androidx.compose.ui.graphics.Color
import dev.icerock.moko.resources.ColorResource

fun ColorResource.Single.toComposeColor(): Color {
	return Color(
		red = color.red,
		green = color.green,
		blue = color.blue,
		alpha = color.alpha
	)
}