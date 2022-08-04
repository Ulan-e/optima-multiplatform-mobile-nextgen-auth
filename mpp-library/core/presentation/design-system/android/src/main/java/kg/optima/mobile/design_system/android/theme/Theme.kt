package kg.optima.mobile.design_system.android.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import kg.optima.mobile.design_system.android.values.ApplicationTypographyData
import kg.optima.mobile.design_system.android.values.LocalTypography
import kg.optima.mobile.design_system.android.values.ThemeColors
import kg.optima.mobile.resources.ComposeColors

object Theme {
	private val LocalColors = staticCompositionLocalOf { LightThemeColors }

	private val LightThemeColors = ThemeColors(
		material = lightColors(
			primary = ComposeColors.PrimaryRed,
			secondary = LightMainGreen,
			background = ComposeColors.Background,
			error = LightRed,
		)
	)

	private val DarkThemeColors = ThemeColors(
		material = darkColors(
			primary = ComposeColors.PrimaryRed,
			secondary = DarkMainGreen,
			background = ComposeColors.Background,
			error = DarkRed,
		)
	)

	@Composable
	fun OptimaTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
		val colors = if (darkTheme) DarkThemeColors else LightThemeColors

		CompositionLocalProvider(
			LocalColors provides colors,
			LocalTypography provides ApplicationTypographyData
		) {
			MaterialTheme(
				colors = colors.material,
				typography = ApplicationTypographyData.typography,
				content = content,
			)
		}
	}
}