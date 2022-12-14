package kg.optima.mobile.android.ui.features.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import dev.icerock.moko.parcelize.Parcelize
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.base.MainContainer
import kg.optima.mobile.android.utils.Constants
import kg.optima.mobile.android.utils.asBaseActivity
import kg.optima.mobile.auth.AuthFeatureFactory
import kg.optima.mobile.auth.presentation.login.LoginIntent
import kg.optima.mobile.auth.presentation.login.LoginState
import kg.optima.mobile.base.di.create
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.checkbox.Checkbox
import kg.optima.mobile.design_system.android.ui.input.InputField
import kg.optima.mobile.design_system.android.ui.input.PasswordInput
import kg.optima.mobile.design_system.android.ui.text_fields.TitleTextField
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarContent
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarInfo
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.values.Deps
import kz.optimabank.optima24.activity.MenuActivity

@Parcelize
object LoginScreen : BaseScreen {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val product = remember {
            AuthFeatureFactory.create<LoginIntent, LoginState>()
        }
        val state = product.state
        val intent = product.intent

        val activity = LocalContext.current.asBaseActivity()

        val onBackClicked =
            activity?.navigationController?.get(Constants.PIN_ENTER_SCREEN_ON_BACK_CLICKED)

        val model by state.stateFlow.collectAsState(
            initial = if (onBackClicked == true) null else UiState.Model.Initial
        )

        val clientIdInputFieldState = remember { mutableStateOf(emptyString) }
        val passwordInputFieldState = remember { mutableStateOf(emptyString) }
        val checkedState = remember { mutableStateOf(true) }

        val signIn: () -> Unit = {
            val info = LoginIntent.SignInInfo.Password(
                clientId = clientIdInputFieldState.value,
                password = passwordInputFieldState.value,
            )
            intent.signIn(info)
        }

        when (val loginState: UiState.Model? = model) {
            null -> intent.showClientId()
            is UiState.Model.Initial -> intent.init()
            is LoginState.Model -> when (loginState) {
                is LoginState.Model.ClientId ->
                    clientIdInputFieldState.value = loginState.clientId
                is LoginState.Model.NavigateTo.PinEnter ->
                    clientIdInputFieldState.value = loginState.clientId
                is LoginState.Model.SignInResult.IncorrectData -> {
                    // TODO incorrect data
                }
                else -> Unit
            }
        }

        MainContainer(
            mainState = model,
            toolbarInfo = ToolbarInfo(content = ToolbarContent.Nothing),
            onBackParameters = true to {
                activity?.navigationController?.set(
                    mapOf(
                        Constants.LOGIN_SCREEN_ON_BACK_CLICKED to true,
                        Constants.PIN_ENTER_SCREEN_ON_BACK_CLICKED to false,
                    )
                )
            },
            contentModifier = Modifier
				.padding(all = Deps.Spacing.standardPadding)
				.background(ComposeColors.Background),
            contentHorizontalAlignment = Alignment.Start,
        ) {
            TitleTextField(
                modifier = Modifier.padding(top = Deps.Spacing.standardMargin * 3),
                text = "????????"
            )
            InputField(
                modifier = Modifier
					.fillMaxWidth()
					.padding(top = Deps.Spacing.marginFromTitle),
                valueState = clientIdInputFieldState,
                hint = "??????????",
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
            PasswordInput(
                modifier = Modifier
					.fillMaxWidth()
					.padding(top = Deps.Spacing.spacing),
                passwordState = passwordInputFieldState,
                hint = "????????????",
                onKeyboardActionDone = signIn,
            )
            Checkbox(
                modifier = Modifier.padding(top = Deps.Spacing.spacing),
                checkedState = checkedState,
                text = "?????????????????? ?????????? ?????? ???????????????? ??????????",
            )
            Spacer(modifier = Modifier.weight(1f))
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = "????????????????????",
                enabled = clientIdInputFieldState.value.length > 1
                        && passwordInputFieldState.value.length > 8,
                onClick = signIn,
            )
        }
    }
}