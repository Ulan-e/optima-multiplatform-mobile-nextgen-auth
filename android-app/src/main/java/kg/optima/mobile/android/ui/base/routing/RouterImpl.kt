package kg.optima.mobile.android.ui.base.routing

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kg.optima.mobile.android.ui.features.BottomNavigationScreen
import kg.optima.mobile.android.ui.features.auth.AuthRouter
import kg.optima.mobile.android.ui.features.auth.login.LoginRouter
import kg.optima.mobile.android.ui.features.welcome.WelcomeRouter
import kg.optima.mobile.auth.presentation.login.LoginState
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.common.presentation.welcome.WelcomeState

object RouterImpl : Router {
	@SuppressLint("ComposableNaming")
	@Composable
	override fun push(stateModels: List<UiState.Model.Navigate>) {
		val navigator = LocalNavigator.currentOrThrow
		compose(stateModels = stateModels).forEach {
			if (it.dropBackStack) {
				navigator.replaceAll(it.screen)
			} else {
				navigator.push(it.screen)
			}
		}
	}

	@Composable
	override fun compose(stateModels: List<UiState.Model.Navigate>): List<RouteInfo> {
		val screens = mutableListOf<RouteInfo>()
		stateModels.forEach {
			val screen = when (it) {
				is WelcomeState.Model.NavigateTo -> RouteInfo(
					screen = WelcomeRouter.compose(stateModel = it),
					dropBackStack = it.dropBackStack
				)
				is LoginState.Model.NavigateTo -> RouteInfo(
					screen = LoginRouter.compose(stateModel = it),
					dropBackStack = it.dropBackStack
				)
//				is CommonScreenModel -> RouteInfo(
//					screen = CommonRouter.compose(stateModel = it),
//					dropBackStack = it.dropBackStack
//				)
//				is MainScreenModel -> RouteInfo(
//					screen = MainRouter.compose(stateModel = it),
//					dropBackStack = it.dropBackStack
//				)
//				is RegistrationScreenModel -> RouteInfo(
//					screen = RegistrationRouter.compose(stateModel = it),
//					dropBackStack = it.dropBackStack
//				)
				else -> RouteInfo(
					screen = BottomNavigationScreen,
					dropBackStack = it.dropBackStack
				)
			}
			screens.add(screen)
		}
		return screens
	}

	@SuppressLint("ComposableNaming")
	@Composable
	override fun popLast() {
		val navigator = LocalNavigator.currentOrThrow
		navigator.pop()
	}
}
