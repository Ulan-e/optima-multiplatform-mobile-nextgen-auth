package kg.optima.mobile.android.ui.features.auth.sms

import androidx.compose.runtime.*
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.features.common.otp.OtpContent
import kg.optima.mobile.auth.presentation.sms.AuthSmsCodeIntent
import kg.optima.mobile.auth.presentation.sms.AuthSmsCodeState
import kg.optima.mobile.base.di.create
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.common.CommonFeatureFactory
import kg.optima.mobile.common.presentation.sms.SmsCodeState
import kg.optima.mobile.core.common.Constants
import kg.optima.mobile.feature.auth.model.AuthOtpModel

@Parcelize
class AuthSmsCodeScreen(
	private val otpModel: AuthOtpModel,
) : BaseScreen {
	@Suppress("NAME_SHADOWING")
	@Composable
	override fun Content() {
		val product = remember {
			CommonFeatureFactory.create<AuthSmsCodeIntent, AuthSmsCodeState>()
		}
		val intent = product.intent
		val state = product.state

		val model by state.stateFlow.collectAsState(initial = UiState.Model.Initial)

		val codeState = remember { mutableStateOf(emptyString) }
		val timeLeftState = remember { mutableStateOf(0) }
		val errorState = remember { mutableStateOf(emptyString) }
		val triesCountState = remember { mutableStateOf(Constants.OTP_MAX_TRIES) }

		when (val model = model) {
			is UiState.Model.Error -> {
				codeState.value = emptyString; errorState.value = model.error
			}
			is SmsCodeState.SmsCodeStateModel.TimeLeft -> {
				timeLeftState.value = model.timeLeft
			}
		}

		OtpContent(
			model = model,
			description = "Мы отправили SMS-код на номер:",
			phoneNumber = otpModel.phoneNumber,
			code = codeState.value,
			timeLeft = timeLeftState.value,
			error = errorState.value,
			triesCount = triesCountState.value,
			onStartTimer = { intent.startTimer(otpModel.timeLeft, System.currentTimeMillis()) },
			onPauseTimer = intent::pauseTimer,
			onValueChanged = {
				if (errorState.value.isNotEmpty()) errorState.value = emptyString
				if (triesCountState.value > 0) codeState.value = it
			},
			onInputCompleted = { intent.onInputCompleted(otpModel.entity, it) },
			onButtonClicked = { intent.onResendSms(otpModel.entity) },
		)
	}
}