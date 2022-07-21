package kg.optima.mobile.design_system.android.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import kg.optima.mobile.design_system.android.values.*

object Theme {
    private val LocalColors = staticCompositionLocalOf { LightThemeColors }

    private val LightThemeColors = MyColors(
        material = lightColors(
            primary = LightMainBackground,
//        primaryVariant = Purple800,
//        onPrimary = Color.White,
            secondary = LightMainGreen,
//        onSecondary = Color.Black,
            background = LightMainBackground,
//        onBackground = Color.Black,
            surface = LightSecondaryBackground,
//        onSurface = Color.Black,
            error = LightRed,
//        onError = Color.White
        ),
        defaultText = LightDefaultText,
        activeText = LightActiveText,
        graySecondaryText = LightGraySecondaryText,
        grayTertiaryText = LightGrayTertiaryText,
        mainGreen = LightMainGreen,
        green = DarkGreen,
        themeColor = WhiteText,
        bothThemeGold = BothThemeGold,
        mainRed = LightRed,
        mainBlue = LightBlue,
        mainBackground = LightMainBackground,
        secondaryBackground = LightSecondaryBackground,
        tertiaryBackground = LightTertiaryBackground,
        alertBackground = LightAlertBackground,
        additionalSecondary = LightAdditionalSecondary,
        additionalTertiary = LightAdditionalTertiary,
        greenBackground = LightGreenBackground
    )

    private val DarkThemeColors = MyColors(
        material = darkColors(
            primary = DarkMainBackground,
            secondary = DarkMainGreen,
            background = DarkMainBackground,
            surface = DarkSecondaryBackground,
            error = DarkRed,
//    onError = Color.Black),
        ),
        defaultText = DarkDefaultText,
        activeText = DarkActiveText,
        graySecondaryText = DarkGraySecondaryText,
        grayTertiaryText = DarkGrayTertiaryText,
        themeColor = BlackText,
        mainGreen = DarkMainGreen,
        green = LightGreen,
        bothThemeGold = BothThemeGold,
        mainRed = DarkRed,
        mainBlue = DarkBlue,
        mainBackground = DarkMainBackground,
        secondaryBackground = DarkSecondaryBackground,
        tertiaryBackground = DarkTertiaryBackground,
        alertBackground = DarkAlertBackground,
        additionalSecondary = DarkAdditionalSecondary,
        additionalTertiary = DarkAdditionalTertiary,
        greenBackground = DarkGreenBackground
    )

    @Composable
    fun OptimaTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
        val colors = if (darkTheme) {
            DarkThemeColors
        } else {
            LightThemeColors
        }

        val typographyData = ApplicationTypographyData.copy(
            subtitleGray = ApplicationTypographyData.subtitleGray.copy(
                color = colors.graySecondaryText
            ),
            hintInput = ApplicationTypographyData.hintInput.copy(
                color = colors.graySecondaryText
            ),
            textInput = ApplicationTypographyData.textInput.copy(
                color = colors.activeText
            )
        )

        CompositionLocalProvider(
            LocalColors provides colors,
            LocalTypography provides typographyData
        ) {
            MaterialTheme(
                colors = colors.material,
                typography = typographyData.typography,
                content = content
            )
        }
    }
}