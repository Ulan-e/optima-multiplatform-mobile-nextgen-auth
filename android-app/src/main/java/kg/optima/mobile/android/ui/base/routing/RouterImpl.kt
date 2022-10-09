package kg.optima.mobile.android.ui.base.routing

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kg.optima.mobile.android.ui.features.BottomNavigationScreen
import kg.optima.mobile.android.ui.features.auth.AuthRouter
import kg.optima.mobile.android.ui.features.biometrics.document_scan.DocumentScanActivity
import kg.optima.mobile.android.ui.features.common.CommonRouter
import kg.optima.mobile.android.ui.features.registration.RegistrationRouter
import kg.optima.mobile.android.utils.navigateTo
import kg.optima.mobile.auth.presentation.AuthNavigateModel
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.common.presentation.welcome.WelcomeState
import kg.optima.mobile.registration.presentation.RegistrationNavigateModel
import kg.optima.mobile.registration.presentation.agreement.AgreementState
import kg.optima.mobile.registration.presentation.create_password.CreatePasswordState
import kg.optima.mobile.registration.presentation.liveness.LivenessState
import kg.optima.mobile.registration.presentation.self_confirm.SelfConfirmState
import kz.verigram.veridoc.sdk.VeridocInitializer

object RouterImpl : Router {
	@SuppressLint("ComposableNaming")
	@Composable
	override fun push(stateModel: UiState.Model.Navigate) {
		if (!stateModel.needLaunchActivity()) {
			val navigator = LocalNavigator.currentOrThrow
			val route = compose(stateModel = stateModel)
			if (route.dropBackStack) {
				navigator.replaceAll(route.screen)
			} else {
				navigator.push(route.screen)
			}
		}
	}

	@Composable
	override fun compose(stateModel: UiState.Model.Navigate): RouteInfo {
		val screen = when (stateModel) {
			is WelcomeState.Model.NavigateTo ->
				CommonRouter.compose(stateModel = stateModel)
			is AuthNavigateModel ->
				AuthRouter.compose(stateModel = stateModel)
			is RegistrationNavigateModel ->
				RegistrationRouter.compose(stateModel = stateModel)
			else ->
				BottomNavigationScreen
		}

		return RouteInfo(
			screen = screen,
			dropBackStack = stateModel.dropBackStack
		)
	}

	@SuppressLint("ComposableNaming")
	@Composable
	override fun popLast() {
		val navigator = LocalNavigator.currentOrThrow
		navigator.pop()
	}

	// Костыль с активити, чтобы освободить экраны от лишней логики
	@SuppressLint("ComposableNaming")
	@Composable
	private fun UiState.Model.Navigate.needLaunchActivity(): Boolean {
		val context = LocalContext.current
		return when (this) {
			SelfConfirmState.Model.NavigateTo.DocumentScan -> {
				VeridocInitializer.init()
				context.navigateTo(DocumentScanActivity())
				true
			}
			else -> false
		}
	}
}
