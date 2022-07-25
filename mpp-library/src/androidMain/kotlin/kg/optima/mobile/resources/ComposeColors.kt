package kg.optima.mobile.resources

import androidx.compose.ui.graphics.Color

object ComposeColors {
	val primaryRed: Color get() = Colors.PrimaryRed.toComposeColor()
	val primaryDisabledGray: Color get() = Colors.PrimaryDisabledGray.toComposeColor()
	val primaryWhite: Color get() = Colors.PrimaryWhite.toComposeColor()
	val primaryBlack: Color get() = Colors.PrimaryBlack.toComposeColor()

	val secondarySystemGreen: Color get() = Colors.SecondarySystemGreen.toComposeColor()
	val secondaryDescriptionGray: Color get() = Colors.SecondaryDescriptionGray.toComposeColor()
}