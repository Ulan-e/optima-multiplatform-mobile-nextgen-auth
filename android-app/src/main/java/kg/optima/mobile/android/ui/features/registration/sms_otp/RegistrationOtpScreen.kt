package kg.optima.mobile.android.ui.features.registration.sms_otp

import androidx.compose.runtime.*
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.features.common.otp.OtpContent
import kg.optima.mobile.base.di.create
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.common.presentation.SmsCodeState
import kg.optima.mobile.core.common.Constants
import kg.optima.mobile.registration.RegistrationFeatureFactory
import kg.optima.mobile.registration.presentation.sms_code.RegistrationSmsCodeIntent
import kg.optima.mobile.registration.presentation.sms_code.RegistrationSmsCodeState

@Parcelize
class RegistrationOtpScreen(
	private val phoneNumber: String,
	private val timeLeft: Long,
	private val referenceId: String,
) : BaseScreen {

	@Suppress("NAME_SHADOWING")
	@Composable
	override fun Content() {
		val product =
			remember { RegistrationFeatureFactory.create<RegistrationSmsCodeIntent, RegistrationSmsCodeState>() }
		val intent = product.intent
		val state = product.state

		val model by state.stateFlow.collectAsState(initial = UiState.Model.Initial)

		val codeState = remember { mutableStateOf(emptyString) }
		val timeLeftState = remember { mutableStateOf(0) }
		val errorState = remember { mutableStateOf(emptyString) }
		val triesCountState = remember { mutableStateOf(Constants.OTP_MAX_TRIES) }

		when (val model = model) {
			is UiState.Model.Error -> {
				codeState.value = emptyString
				errorState.value = model.error
			}
			is RegistrationSmsCodeState.SmsCodeStateModel.Request -> {
				triesCountState.value = Constants.OTP_MAX_TRIES
				codeState.value = emptyString
				errorState.value = emptyString
			}
			is SmsCodeState.SmsCodeStateModel.TimeLeft -> {
				timeLeftState.value = model.timeLeft
			}
		}

		OtpContent(
			model = model,
			description = "Вводя код из SMS вы подписываете оферту, подтверждая свое согласие\n" +
					"Мы отправили SMS на номер:",
			phoneNumber = phoneNumber,
			code = codeState.value,
			timeLeft = timeLeftState.value,
			error = errorState.value,
			triesCount = triesCountState.value,
			onStartTimer = { intent.startTimer(timeLeft, System.currentTimeMillis()) },
			onPauseTimer = intent::pauseTimer,
			onValueChanged = {
				if (errorState.value.isNotEmpty()) errorState.value = emptyString
				if (triesCountState.value > 0) codeState.value = it
			},
			onInputCompleted = {
				if (triesCountState.value > 0) {
					triesCountState.value--; intent.smsCodeEntered(phoneNumber, it, referenceId)
				}
			},
			onButtonClicked = { intent.smsCodeRequest(phoneNumber, System.currentTimeMillis()) }
		)
	}
}