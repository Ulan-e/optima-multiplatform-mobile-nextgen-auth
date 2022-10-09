package kg.optima.mobile.android.ui.base.routing

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import kg.optima.mobile.base.presentation.UiState

interface Router {
	@SuppressLint("ComposableNaming")
	@Composable
	fun push(stateModel: UiState.Model.Navigate)

	@Composable
	fun compose(stateModel: UiState.Model.Navigate): RouteInfo

	@SuppressLint("ComposableNaming")
	@Composable
	fun popLast()
}