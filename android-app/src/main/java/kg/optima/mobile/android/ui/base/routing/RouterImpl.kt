package kg.optima.mobile.android.ui.base.routing

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kg.optima.mobile.android.ui.features.BottomNavigationScreen
import kg.optima.mobile.android.ui.features.auth.AuthRouter
import kg.optima.mobile.android.ui.features.common.CommonRouter
import kg.optima.mobile.android.ui.features.main.MainRouter
import kg.optima.mobile.android.ui.features.registration.RegistrationRouter
import kg.optima.mobile.android.ui.features.welcome.WelcomeRouter
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.feature.auth.AuthScreenModel
import kg.optima.mobile.feature.common.CommonScreenModel
import kg.optima.mobile.feature.main.MainScreenModel
import kg.optima.mobile.feature.registration.RegistrationScreenModel
import kg.optima.mobile.feature.welcome.WelcomeScreenModel

object RouterImpl : Router {
	@SuppressLint("ComposableNaming")
	@Composable
	override fun push(screenModels: List<ScreenModel>) {
		val navigator = LocalNavigator.currentOrThrow
		compose(screenModels = screenModels).forEach {
			if (it.dropBackStack) {
				navigator.replaceAll(it.screen)
			} else {
				navigator.push(it.screen)
			}
		}
	}

	@Composable
	override fun compose(screenModels: List<ScreenModel>): List<RouteInfo> {
		val screens = mutableListOf<RouteInfo>()
		screenModels.forEach {
			val screen = when (it) {
				is WelcomeScreenModel -> RouteInfo(
					screen = WelcomeRouter.compose(screenModel = it),
					dropBackStack = it.dropBackStack
				)
				is AuthScreenModel -> RouteInfo(
					screen = AuthRouter.compose(screenModel = it),
					dropBackStack = it.dropBackStack
				)
				is CommonScreenModel -> RouteInfo(
					screen = CommonRouter.compose(screenModel = it),
					dropBackStack = it.dropBackStack
				)
				is MainScreenModel -> RouteInfo(
					screen = MainRouter.compose(screenModel = it),
					dropBackStack = it.dropBackStack
				)
				is RegistrationScreenModel -> RouteInfo(
					screen = RegistrationRouter.compose(screenModel = it),
					dropBackStack = it.dropBackStack
				)
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
