package kg.optima.mobile.android.ui.common

import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo

fun BottomSheetNavigator.show(
	info: BottomSheetInfo,
) = show(BottomSheetScreen(info))