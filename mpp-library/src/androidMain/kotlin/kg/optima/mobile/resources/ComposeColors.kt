package kg.optima.mobile.resources

import androidx.compose.ui.graphics.Color

object ComposeColors {
	val primaryRed: Color get() = Colors.PrimaryRed.toComposeColor()
	val primaryDisabledGray: Color get() = Colors.PrimaryDisabledGray.toComposeColor()
	val primaryWhite: Color get() = Colors.PrimaryWhite.toComposeColor()
	val primaryBlack: Color get() = Colors.PrimaryBlack.toComposeColor()

	val secondaryBackground: Color get() = Colors.SecondaryBackground.toComposeColor()
	val secondaryGreen: Color get() = Colors.SecondaryGreen.toComposeColor()
	val secondarySystemGreen: Color get() = Colors.SecondarySystemGreen.toComposeColor()
	val secondaryDescriptionGray: Color get() = Colors.SecondaryDescriptionGray.toComposeColor()

	val opaquedDisabledGray: Color get() = secondaryDescriptionGray.copy(alpha = 0.08f)
}