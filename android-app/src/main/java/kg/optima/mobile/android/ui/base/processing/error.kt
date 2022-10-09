package kg.optima.mobile.android.ui.base.processing

import androidx.compose.runtime.Composable
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView
import kg.optima.mobile.design_system.android.utils.resources.ComposeColor
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors

@Composable
fun processError(
	errorState: UiState.Model.Error,
	onSheetStateChanged: (sheetInfo: BottomSheetInfo?) -> Unit,
	onBottomSheetHidden: () -> Unit,
) {
	// TODO process error
	when (errorState) {
		is UiState.Model.Error.ApiError -> onSheetStateChanged(
			BottomSheetInfo(
				title = errorState.error,
				buttons = listOf(
					ButtonView.Primary(
						text = "Повторить попытку",
						composeColor = ComposeColor.composeColor(ComposeColors.Green),
						onClickListener = ButtonView.onClickListener(
							onBottomSheetHidden
						)
					)
				)
			)
		)
		else -> Unit
	}
}