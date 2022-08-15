package kg.optima.mobile.android.ui.welcome

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kg.optima.mobile.android.ui.common.show
import kg.optima.mobile.android.ui.login.LoginScreen
import kg.optima.mobile.android.ui.pin.PinSetScreen
import kg.optima.mobile.android.utils.appVersion
import kg.optima.mobile.auth.domain.usecase.login.GrantType
import kg.optima.mobile.auth.presentation.auth_state.AuthStateFactory
import kg.optima.mobile.auth.presentation.auth_state.AuthStateIntentHandler
import kg.optima.mobile.auth.presentation.auth_state.AuthStatusStateMachine
import kg.optima.mobile.base.presentation.StateMachine
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.buttons.TransparentButton
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView
import kg.optima.mobile.design_system.android.ui.containers.MainContainer
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.images.MainImages
import kg.optima.mobile.design_system.android.utils.resources.resId


@Suppress("NAME_SHADOWING")
object WelcomeScreen : Screen {

	@Composable
	override fun Content() {
		val stateMachine: AuthStatusStateMachine = AuthStateFactory.stateMachine
		val intentHandler: AuthStateIntentHandler = AuthStateFactory.intentHandler

		val navigator = LocalNavigator.currentOrThrow
		val bottomSheetNavigator = LocalBottomSheetNavigator.current

		val state by stateMachine.state.collectAsState(initial = null)

		when (val state = state) {
			is AuthStatusStateMachine.AuthStatusState -> {
				val items = mutableListOf<Screen>(LoginScreen(state.clientId))
				when (state) {
					is AuthStatusStateMachine.AuthStatusState.Authorized -> {
						if (state.grantTypes.contains(GrantType.Pin)) {
							items.add(PinSetScreen)
						}
						if (state.grantTypes.contains(GrantType.Biometry)) {
							// TODO add biometry
						}
					}
					is AuthStatusStateMachine.AuthStatusState.NotAuthorized -> Unit
				}
				navigator.push(items)
			}
			is StateMachine.State.Loading ->
				Log.d("WelcomeScreen", "Loading State")
			is StateMachine.State.Error ->
				Log.d("WelcomeScreen", "Error State")
			null -> Unit
		}
//		bottomSheetNavigator.show(BottomSheetInfo(
//			title = "Пароль не совпадает\nс предыдущим",
//			buttons = listOf(
//				ButtonView.Primary(
//					modifier = Modifier.fillMaxWidth(),
//					text = "Повторить попытку",
//					color = ComposeColors.Green,
//					onClick = { bottomSheetNavigator.hide() }
//				)
//			)
//		))

		MainContainer {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(all = Deps.Spacing.standardPadding),
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				Image(
					modifier = Modifier
						.padding(top = 60.dp)
						.size(width = 170.dp, height = 36.dp),
					painter = painterResource(
						id = MainImages.optimaLogo.resId()
					),
					contentDescription = "Optima24",
				)
				WelcomeScreenButtonBlock(
					modifier = Modifier.weight(1f),
				)
				PrimaryButton(
					modifier = Modifier.fillMaxWidth(),
					text = "Войти",
					onClick = { intentHandler.checkIsAuthorized() },
				)
				TransparentButton(
					modifier = Modifier
						.fillMaxWidth()
						.padding(
							top = Deps.Spacing.standardMargin,
							bottom = Deps.Spacing.standardMargin,
						),
					text = "Зарегистрироваться",
				)
				Text(
					text = "Версия $appVersion",
					fontSize = Headings.H6.px.sp,
					color = ComposeColors.DescriptionGray,
				)
			}
		}
	}

	private fun AuthStateIntentHandler.checkIsAuthorized() =
		this.dispatch(AuthStateIntentHandler.CheckIsAuthorizedIntent)
}