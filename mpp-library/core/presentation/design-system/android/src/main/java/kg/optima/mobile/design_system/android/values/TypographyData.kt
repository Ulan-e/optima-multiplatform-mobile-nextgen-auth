package kg.optima.mobile.design_system.android.values

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle

data class TypographyData(
    val typography: Typography,
    val subtitleGray: TextStyle,
    val hintInput: TextStyle,
    val textInput: TextStyle,
) {
    val h1: TextStyle get() = typography.h1
    val h2: TextStyle get() = typography.h2
    val h3: TextStyle get() = typography.h3
    val h4: TextStyle get() = typography.h4
    val h5: TextStyle get() = typography.h5
    val h6: TextStyle get() = typography.h6
    val subtitle1: TextStyle get() = typography.subtitle1
    val subtitle2: TextStyle get() = typography.subtitle2
    val body1: TextStyle get() = typography.body1
    val body2: TextStyle get() = typography.body2
    val button: TextStyle get() = typography.button
    val caption: TextStyle get() = typography.caption
}