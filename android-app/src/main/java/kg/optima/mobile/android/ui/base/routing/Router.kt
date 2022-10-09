package kg.optima.mobile.android.ui.base.routing

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import kg.optima.mobile.base.presentation.UiState

interface Router {
	@SuppressLint("ComposableNaming")
	@Composable
	fun push(stateModels: List<UiState.Model.Navigate>)

	@SuppressLint("ComposableNaming")
	@Composable
	fun push(stateModel: UiState.Model.Navigate) = push(stateModels = listOf(stateModel))

	@Composable
	fun compose(stateModels: List<UiState.Model.Navigate>): List<RouteInfo>

	@SuppressLint("ComposableNaming")
	@Composable
	fun popLast()
}