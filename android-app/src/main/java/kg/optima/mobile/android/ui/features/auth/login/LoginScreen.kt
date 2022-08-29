package kg.optima.mobile.android.ui.features.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.auth.presentation.login.LoginFactory
import kg.optima.mobile.auth.presentation.login.LoginIntentHandler
import kg.optima.mobile.auth.presentation.login.LoginStateMachine
import kg.optima.mobile.base.presentation.StateMachine
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.checkbox.Checkbox
import kg.optima.mobile.design_system.android.ui.input.InputField
import kg.optima.mobile.design_system.android.ui.input.PasswordInput
import kg.optima.mobile.design_system.android.ui.text_fields.TitleTextField
import kg.optima.mobile.design_system.android.ui.toolbars.MainToolbar
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.values.Deps


object LoginScreen : Screen {

	@Composable
	override fun Content() {
		val stateMachine: LoginStateMachine = LoginFactory.stateMachine
		val intentHandler: LoginIntentHandler = LoginFactory.intentHandler

		val state by stateMachine.state.collectAsState(initial = StateMachine.State.Initial)

		val clientIdInputFieldState = remember { mutableStateOf(emptyString) }
		val passwordInputFieldState = remember { mutableStateOf(emptyString) }
		val checkedState = remember { mutableStateOf(true) }

		when (val loginState = state) {
			is StateMachine.State.Initial ->
				intentHandler.dispatch(LoginIntentHandler.LoginIntent.GetClientId)
			is LoginStateMachine.LoginState.ClientId ->
				clientIdInputFieldState.value = loginState.clientId.orEmpty()
		}

		MainContainer(mainState = state) {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.background(ComposeColors.Background),
			) {
				MainToolbar(onBackClick = {
					intentHandler.pop()
				})
				Column(
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = Deps.Spacing.standardPadding)
						.weight(1f),
				) {
					TitleTextField(
						modifier = Modifier.padding(top = Deps.Spacing.standardMargin * 3),
						text = "Авторизация"
					)
					InputField(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = Deps.Spacing.marginFromTitle),
						valueState = clientIdInputFieldState,
						hint = "Client ID",
						keyboardType = KeyboardType.Number,
						bottomActionButton = "Запросить Client ID" to {
							// TODO get clientid
						}
					)
					PasswordInput(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = Deps.Spacing.spacing),
						passwordState = passwordInputFieldState,
						hint = "Пароль",
					)
					Checkbox(
						modifier = Modifier.padding(top = Deps.Spacing.spacing),
						checkedState = checkedState,
						text = "Запомнить логин",
					)
				}
				PrimaryButton(
					modifier = Modifier
						.fillMaxWidth()
						.padding(all = Deps.Spacing.standardPadding),
					text = "Продолжить",
					onClick = {
						intentHandler.dispatch(
							LoginIntentHandler.LoginIntent.SignIn.Password(
								clientId = clientIdInputFieldState.value,
								password = passwordInputFieldState.value,
							)
						)
					},
				)
			}
		}
	}
}