package kg.optima.mobile.design_system.android.values

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Deps {
	object Spacing {
		val standardMargin get() = 16.dp
		val standardPadding get() = 20.dp

		val minPadding get() = 1.dp

		val marginFromTitle get() = standardMargin * 2
		val marginFromInput get() = 8.dp
		val subheaderMargin get() = 8.dp

		val spacing get() = 24.dp

		val pinBtnYMargin get() = 36.dp
		val pinCellXMargin get() = 10.dp
		val numPadXMargin get() = 40.dp

		val swiperTopMargin get() = 6.dp

		val rowElementMargin get() = 10.dp
		val colElementMargin get() = 10.dp

		val iconDescriptionMargin get() = 4.dp

		val bigMarginTop get() = 80.dp
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
		val pinDotSize get() = 10.dp

		val passValidityDotSize get() = 8.dp

		val sheetSwiperSize get() = 48.dp to 3.dp
	}

	object TextSize {
		val pinBtnText get() = 32.sp
		val codeInputSymbol get() = 40.sp
	}

	val cornerRadius get() = 8.dp
	val borderStroke get() = 1.dp

	val mainButtonCornerRadius get() = 4.dp
	val inputFieldCornerRadius get() = 10.dp
	val checkboxCornerRadius get() = 6.dp
	val cardCornerRadius get() = 16.dp
}