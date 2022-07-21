package kg.optima.mobile.resources

import androidx.compose.ui.graphics.Color

public object ComposeColors {
	public val primaryRed: Color get() = Colors.PrimaryRed.toComposeColor()
	public val primaryDisabledGrey: Color get() = Colors.PrimaryDisabledGrey.toComposeColor()
	public val primaryWhite: Color get() = Colors.PrimaryWhite.toComposeColor()
	public val primaryBlack: Color get() = Colors.PrimaryBlack.toComposeColor()

	public val secondarySystemGreen: Color get() = Colors.SecondarySystemGreen.toComposeColor()
}