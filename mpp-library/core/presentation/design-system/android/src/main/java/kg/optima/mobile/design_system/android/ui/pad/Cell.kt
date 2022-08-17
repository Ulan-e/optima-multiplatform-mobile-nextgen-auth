package kg.optima.mobile.design_system.android.ui.pad

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

sealed interface Cell {
	val onClick: OnCellClick

	class Num(val num: String, override val onClick: OnCellClick) : Cell
	class Text(val text: String, val color: Color, override val onClick: OnCellClick) : Cell
	class Img(@DrawableRes val resId: Int, override val onClick: OnCellClick) : Cell
}

typealias OnCellClick = (Cell) -> Unit