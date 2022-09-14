package kg.optima.mobile.design_system.android.ui.buttons.model

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.design_system.android.utils.resources.ComposeColor
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors

sealed interface ButtonSecondaryType : Parcelable {
	val composeColor: ComposeColor

	@Parcelize
	class Main(
		override val composeColor: ComposeColor = ComposeColor.composeColor(ComposeColors.PrimaryRed)
	) : ButtonSecondaryType

	@Parcelize
	class Tertiary(
		override val composeColor: ComposeColor = ComposeColor.composeColor(ComposeColors.PrimaryDisabledGray)
	) : ButtonSecondaryType
}