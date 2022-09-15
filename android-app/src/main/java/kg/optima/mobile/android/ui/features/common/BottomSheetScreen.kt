package kg.optima.mobile.android.ui.features.common

import androidx.compose.runtime.Composable
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheet
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo

@Parcelize
class BottomSheetScreen(
	private val info: BottomSheetInfo,
) : BaseScreen {
	@Composable
	override fun Content() = BottomSheet(info)
}