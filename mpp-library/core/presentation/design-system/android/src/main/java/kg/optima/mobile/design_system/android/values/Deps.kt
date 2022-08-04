package kg.optima.mobile.design_system.android.values

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Deps {
	object Spacing {
		val standardMargin get() = 16.dp
		val standardPadding get() = 20.dp

		val marginFromTop get() = standardMargin * 6
		val marginFromTitle get() = standardMargin * 2
		val marginFromInput get() = 8.dp
		val marginFromHeader get() = 54.dp
		val subheaderMargin get() = 8.dp

		val pinBtnXMargin get() = 36.dp
		val pinBtnYMargin get() = 36.dp

		val pinCellXMargin get() = 10.dp

		val spacing get() = 24.dp
	}

	object Size {
		val buttonHeight get() = 50.dp
		val mainButtonSize get() = 44.dp to 40.dp
		val mainButtonImageSize get() = 24.dp
		val trailingIconSize get() = 20.dp

		val checkboxSize get() = 24.dp

		val pinCellSize get() = 58.dp to 64.dp

		val invisible get() = 0.dp

		val pinBtnSize get() = 75.dp
	}

	object TextSize {
		val pinBtnText get() = 32.sp
	}

	val cornerRadius get() = 8.dp
	val borderStroke get() = 1.dp

	val mainButtonCornerRadius get() = 4.dp

	val inputFieldCornerRadius get() = 10.dp

	val checkboxCornerRadius get() = 6.dp
}