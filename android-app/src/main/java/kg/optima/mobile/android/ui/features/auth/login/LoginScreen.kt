package kg.optima.mobile.android.ui.features.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.auth.AuthFeatureFactory
import kg.optima.mobile.auth.presentation.login.LoginIntent
import kg.optima.mobile.auth.presentation.login.LoginState
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.checkbox.Checkbox
import kg.optima.mobile.design_system.android.ui.input.InputField
import kg.optima.mobile.design_system.android.ui.input.PasswordInput
import kg.optima.mobile.design_system.android.ui.text_fields.TitleTextField
import kg.optima.mobile.design_system.android.ui.toolbars.MainToolbar
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.values.Deps


class LoginScreen(
	private val nextScreenModel: ScreenModel,
) : Screen {

	@Composable
	override fun Content() {
		val product = remember {
			AuthFeatureFactory.create<LoginIntent, LoginState>(nextScreenModel)
		}
		val state = product.state
		val intent = product.intent

		val model by state.stateFlow.collectAsState(initial = State.StateModel.Initial)

		val clientIdInputFieldState = remember { mutableStateOf(emptyString) }
		val passwordInputFieldState = remember { mutableStateOf(emptyString) }
		val checkedState = remember { mutableStateOf(true) }

		when (val loginState = model) {
			is State.StateModel.Initial ->
				intent.getClientId()
			is LoginState.LoginStateModel.ClientId ->
				clientIdInputFieldState.value = loginState.clientId.orEmpty()
		}

		val signIn: () -> Unit = {
			intent.signIn(
				info = LoginIntent.SignInInfo.Password(
					clientId = clientIdInputFieldState.value,
					password = passwordInputFieldState.value,
				)
			)
		}

		MainContainer(mainState = model) {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.background(ComposeColors.Background),
			) {
				MainToolbar(onBackClick = {
					intent.pop()
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
						imeAction = ImeAction.Next,
						bottomActionButton = "Запросить Client ID" to {
							// TODO get clientid
						},
					)
					PasswordInput(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = Deps.Spacing.spacing),
						passwordState = passwordInputFieldState,
						hint = "Пароль",
						onKeyboardActionDone = signIn,
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
					onClick = signIn,
				)
			}
		}
	}
}