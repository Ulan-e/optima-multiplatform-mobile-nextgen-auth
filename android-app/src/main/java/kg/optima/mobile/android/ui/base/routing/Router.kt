package kg.optima.mobile.android.ui.base.routing

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import kg.optima.mobile.core.navigation.ScreenModel

interface Router {
	@SuppressLint("ComposableNaming")
	@Composable
	fun push(screenModels: List<ScreenModel>)

	@Composable
	fun compose(screenModels: List<ScreenModel>): List<RouteInfo>

	@SuppressLint("ComposableNaming")
	@Composable
	fun popLast()
}