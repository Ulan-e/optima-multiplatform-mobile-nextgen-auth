package kg.optima.mobile.design_system.android.ui.pad

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

sealed interface CellType {
	class Num(val num: String) : CellType
	class Text(val text: String, val color: Color) : CellType
	class Img(@DrawableRes val resId: Int) : CellType
}