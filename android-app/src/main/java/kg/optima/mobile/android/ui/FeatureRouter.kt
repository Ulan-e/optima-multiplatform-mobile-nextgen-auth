package kg.optima.mobile.android.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.core.navigation.ScreenModel

interface FeatureRouter<In : ScreenModel> {
	@Composable
	fun compose(screenModel: In): Screen
}