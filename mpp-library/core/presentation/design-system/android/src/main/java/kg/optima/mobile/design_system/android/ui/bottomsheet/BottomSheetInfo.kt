package kg.optima.mobile.design_system.android.ui.bottomsheet

import androidx.compose.runtime.Composable
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView

class BottomSheetInfo(
	val title: String,
	val description: String? = null,
	val content: @Composable () -> Unit = {},
	val buttons: List<ButtonView>,
)