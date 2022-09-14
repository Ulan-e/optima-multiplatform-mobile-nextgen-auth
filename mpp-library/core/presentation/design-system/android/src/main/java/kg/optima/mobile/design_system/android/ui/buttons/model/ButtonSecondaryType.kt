package kg.optima.mobile.design_system.android.ui.buttons.model

import androidx.compose.ui.graphics.Color
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors

sealed interface ButtonSecondaryType {
	val color: Color

	class Main(override val color: Color = ComposeColors.PrimaryRed) : ButtonSecondaryType
	class Tertiary(override val color: Color = ComposeColors.PrimaryDisabledGray) : ButtonSecondaryType
}