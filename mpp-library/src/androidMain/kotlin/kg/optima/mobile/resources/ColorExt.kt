package kg.optima.mobile.resources

import androidx.compose.ui.graphics.Color
import dev.icerock.moko.resources.ColorResource

public fun ColorResource.Single.toComposeColor(): Color {
	return Color(
		red = color.red,
		green = color.green,
		blue = color.blue,
		alpha = color.alpha
	)
}