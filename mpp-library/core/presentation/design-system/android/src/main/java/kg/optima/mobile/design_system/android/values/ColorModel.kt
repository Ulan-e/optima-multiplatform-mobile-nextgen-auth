package kg.optima.mobile.design_system.android.values

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color


data class MyColors(
    val material: Colors,
    val defaultText: Color,
    val themeColor: Color,
    val activeText: Color,
    val graySecondaryText: Color,
    val grayTertiaryText: Color,
    val mainGreen: Color,
    val green: Color,
    val bothThemeGold: Color,
    val mainRed: Color,
    val mainBlue: Color,
    val mainBackground: Color,
    val secondaryBackground : Color,
    val tertiaryBackground: Color,
    val alertBackground: Color,
    val additionalSecondary : Color,
    val additionalTertiary : Color,
    val greenBackground : Color
) {
    val primary: Color get() = material.primary
    val primaryVariant: Color get() = material.primaryVariant
    val secondary: Color get() = material.secondary
    val secondaryVariant: Color get() = material.secondaryVariant
    val background: Color get() = material.background
    val surface: Color get() = material.surface
    val error: Color get() = material.error
    val onPrimary: Color get() = material.onPrimary
    val onSecondary: Color get() = material.onSecondary
    val onBackground: Color get() = material.onBackground
    val onSurface: Color get() = material.onSurface
    val onError: Color get() = material.onError
    val isLight: Boolean get() = material.isLight

}