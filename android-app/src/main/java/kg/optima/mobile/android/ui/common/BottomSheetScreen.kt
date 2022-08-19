package kg.optima.mobile.android.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheet
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import java.lang.System.currentTimeMillis

class BottomSheetScreen(
	private val info: BottomSheetInfo,
) : Screen {
	override val key: ScreenKey
		get() = currentTimeMillis().toString()

	@Composable
	override fun Content() = BottomSheet(info)
}