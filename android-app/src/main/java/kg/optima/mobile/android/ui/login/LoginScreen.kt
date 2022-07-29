package kg.optima.mobile.android.ui.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kg.optima.mobile.auth.domain.usecase.login.GrantType
import kg.optima.mobile.auth.presentation.login.LoginFactory
import kg.optima.mobile.auth.presentation.login.LoginIntentHandler
import kg.optima.mobile.auth.presentation.login.LoginStateMachine
import kg.optima.mobile.base.presentation.StateMachine
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.checkbox.Checkbox
import kg.optima.mobile.design_system.android.ui.input.InputField
import kg.optima.mobile.design_system.android.ui.input.PasswordInput
import kg.optima.mobile.design_system.android.ui.text_fields.TitleTextField
import kg.optima.mobile.design_system.android.ui.toolbars.MainToolbar
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.ComposeColors


object LoginScreen : Screen {

	@Composable
	override fun Content() {
		val stateMachine: LoginStateMachine = LoginFactory.stateMachine
		val intentHandler: LoginIntentHandler = LoginFactory.intentHandler

		val navigator = LocalNavigator.currentOrThrow

		val state by stateMachine.state.collectAsState(initial = null)

		when (state) {
			is LoginStateMachine.LoginState ->
				Log.d("MainScreen", "Success State")
			is StateMachine.State.Loading ->
				Log.d("MainScreen", "Loading State")
			is StateMachine.State.Error ->
				Log.d("MainScreen", "Error State")
			null -> Unit
		}

		val clientIdInputFieldState = remember { mutableStateOf("") }
		val passwordInputFieldState = remember { mutableStateOf("") }
		val checkedState = remember { mutableStateOf(true) }

		Column(
			modifier = Modifier
				.fillMaxSize()
				.background(ComposeColors.secondaryBackground),
		) {
			MainToolbar(
				onBackClick = { navigator.pop() }
			)
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = Deps.standardPadding)
					.weight(1f),
			) {
				TitleTextField(
					modifier = Modifier.padding(top = Deps.standardMargin * 3),
					text = "Авторизация"
				)
				InputField(
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = Deps.marginFromTitle),
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
						.padding(top = Deps.spacing),
					passwordState = passwordInputFieldState,
					hint = "Пароль",
				)
				Checkbox(
					modifier = Modifier.padding(top = Deps.spacing),
					checkedState = checkedState,
					text = "Запомнить логин",
				)
			}
			PrimaryButton(
				modifier = Modifier
					.fillMaxWidth()
					.padding(all = Deps.standardPadding),
				text = "Продолжить",
				onClick = {
					intentHandler.dispatch(LoginIntentHandler.LoginIntent(
						clientId = clientIdInputFieldState.value,
						password = passwordInputFieldState.value,
						grantType = GrantType.Password
					))
				},
			)
		}
	}
}