package kg.optima.mobile.android.ui.features.auth.pin.enter

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.base.MainContainer
import kg.optima.mobile.android.utils.Constants
import kg.optima.mobile.android.utils.asActivity
import kg.optima.mobile.android.utils.asBaseActivity
import kg.optima.mobile.auth.AuthFeatureFactory
import kg.optima.mobile.auth.presentation.login.LoginIntent
import kg.optima.mobile.auth.presentation.pin_enter.PinEnterIntent
import kg.optima.mobile.auth.presentation.pin_enter.PinEnterState
import kg.optima.mobile.base.di.createWithStateParam
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.ui.screens.pin.ActionCell
import kg.optima.mobile.design_system.android.ui.screens.pin.PinScreen
import kg.optima.mobile.design_system.android.ui.screens.pin.headers.enterPinScreenHeader
import kg.optima.mobile.design_system.android.utils.biometry.BiometryManager

@Parcelize
object PinEnterScreen : BaseScreen {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val product = remember {
            AuthFeatureFactory.createWithStateParam<PinEnterIntent, PinEnterState>()
        }
        val state = product.state
        val intent = product.intent

        val activity = LocalContext.current.asBaseActivity()

        val model by state.stateFlow.collectAsState(initial = UiState.Model.Initial)

        val context = LocalContext.current

        val codeState = remember { mutableStateOf(emptyString) }
        val errorState = remember { mutableStateOf(Pair(false, 0)) }

        val onBiometryAuthenticate: () -> Unit = {
            BiometryManager.authorize(
                activity = context.asActivity(),
                doOnSuccess = { intent.signIn(LoginIntent.SignInInfo.Biometry) },
            )
        }

        when (val pinEnterModel: UiState.Model? = model) {
            is UiState.Model.Initial -> intent.init()
            is UiState.Model.Error.BaseError -> {
                errorState.value = Pair(first = true, second = intent.attempts())
                intent.decreaseAttempts()
            }
            is PinEnterState.Model.Biometry -> {
                if (pinEnterModel.enabled) {
                    onBiometryAuthenticate()
                } else {
                    // TODO hide biometry authenticate btn
                }
            }
        }

        MainContainer(
            mainState = model,
            toolbarInfo = null,
            onBackParameters = true to {
                activity?.navigationController?.set(
                    key = Constants.PIN_ENTER_SCREEN_ON_BACK_CLICKED,
                    value = true,
                )
            }
        ) { onBack ->
            PinScreen(
                header = enterPinScreenHeader(
                    onCloseClick = onBack,
                    onLogoutClick = intent::logout,
                    username = intent.username()
                ),
                codeState = codeState,
                error = errorState.value,
                onValueChanged = {
                    if (errorState.value.first && it.isNotEmpty()) {
                        errorState.value = Pair(false, 0)
                    }
                },
                onInputCompleted = { pin ->
                    intent.signIn(LoginIntent.SignInInfo.Pin(pin = pin))
                    codeState.value = ""
                },
                actionCell = ActionCell.FingerPrint(
                    onCellClick = { onBiometryAuthenticate() },
                )
            )
        }
    }

}