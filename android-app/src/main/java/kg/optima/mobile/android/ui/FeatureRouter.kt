package kg.optima.mobile.android.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.base.presentation.UiState

interface FeatureRouter<In : UiState.Model> {
	@Composable
	fun compose(stateModel: In): Screen
}