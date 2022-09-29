package kg.optima.mobile.android.ui.features.auth.pin

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.base.MainContainer
import kg.optima.mobile.android.utils.asActivity
import kg.optima.mobile.auth.AuthFeatureFactory
import kg.optima.mobile.auth.presentation.login.LoginIntent
import kg.optima.mobile.auth.presentation.login.LoginState
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.design_system.android.ui.screens.pin.ActionCell
import kg.optima.mobile.design_system.android.ui.screens.pin.PinScreen
import kg.optima.mobile.design_system.android.ui.screens.pin.headers.enterPinScreenHeader
import kg.optima.mobile.design_system.android.utils.biometry.BiometryManager

@Parcelize
class PinEnterScreen(
	private val showBiometry: Boolean = false,
	private val nextScreenModel: ScreenModel,
) : BaseScreen {

	@OptIn(ExperimentalMaterialApi::class)
	@Composable
	override fun Content() {
		val product = remember {
			AuthFeatureFactory.create<LoginIntent, LoginState>(nextScreenModel)
		}
		val state = product.state
		val intent = product.intent

		val model by state.stateFlow.collectAsState(
			initial = if (showBiometry) LoginState.LoginStateModel.ShowBiometry else null
		)

		val context = LocalContext.current

		val codeState = remember { mutableStateOf(emptyString) }

		val onBiometryAuthenticateSuccess: () -> Unit = {
			intent.signIn(LoginIntent.SignInInfo.Biometry)
		}
		val onBiometryAuthenticate: () -> Unit = {
			BiometryManager.authorize(
				activity = context.asActivity(),
				doOnSuccess = onBiometryAuthenticateSuccess,
				doOnFailure = {},
			)
		}

		when (model) {
			is LoginState.LoginStateModel.ShowBiometry -> onBiometryAuthenticate()
		}

		MainContainer(
			mainState = model,
			toolbarInfo = null,
		) { onBack ->
			PinScreen(
				header = enterPinScreenHeader(
					onCloseClick = { onBack() },
					onLogoutClick = {},
				),
				codeState = codeState,
				onInputCompleted = { pin ->
					intent.signIn(LoginIntent.SignInInfo.Pin(pin = pin))
				},
				actionCell = ActionCell.FingerPrint(
					onCellClick = { onBiometryAuthenticate() },
				)
			)
		}
	}

}