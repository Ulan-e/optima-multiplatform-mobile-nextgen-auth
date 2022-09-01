package kg.optima.mobile.android.ui.features.common

import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo

fun BottomSheetNavigator.show(
	info: BottomSheetInfo,
) = show(BottomSheetScreen(info))