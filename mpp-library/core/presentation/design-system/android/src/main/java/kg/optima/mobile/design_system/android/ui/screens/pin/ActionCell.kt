package kg.optima.mobile.design_system.android.ui.screens.pin

import android.content.Context
import kg.optima.mobile.design_system.android.ui.pad.Cell
import kg.optima.mobile.design_system.android.ui.pad.OnCellClick
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.resId
import kg.optima.mobile.images.resId
import kg.optima.mobile.resources.images.MainImages

sealed interface ActionCell {
	val onCellClick: OnCellClick
	val cell: Cell

	class Close(override val onCellClick: OnCellClick) : ActionCell {
		override val cell: Cell = Cell.Text(
			text = "Закрыть",
			color = ComposeColors.PrimaryRed,
			onClick = onCellClick,
		)
	}

	class FingerPrint(override val onCellClick: OnCellClick) : ActionCell {
		override val cell: Cell = Cell.Img(
			resId = MainImages.fingerprint.resId(),
			onClick = onCellClick,
		)
	}
}