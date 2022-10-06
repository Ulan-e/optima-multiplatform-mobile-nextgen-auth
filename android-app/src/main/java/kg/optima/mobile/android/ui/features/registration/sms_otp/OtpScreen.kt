package kg.optima.mobile.android.ui.features.registration.sms_otp

import android.text.format.DateUtils
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.base.di.create
import kg.optima.mobile.base.presentation.BaseMppState
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.core.common.Constants
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView
import kg.optima.mobile.design_system.android.ui.input.CodeInput
import kg.optima.mobile.design_system.android.ui.text_fields.TitleTextField
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.registration.RegistrationFeatureFactory
import kg.optima.mobile.registration.presentation.sms_code.SmsCodeIntent
import kg.optima.mobile.registration.presentation.sms_code.SmsCodeState
import kg.optima.mobile.resources.Headings

@Parcelize
class OtpScreen(
	private val phoneNumber: String,
	private val timeLeft: Long,
	private val referenceId: String,
) : BaseScreen {

	@OptIn(ExperimentalMaterialApi::class)
	@Suppress("NAME_SHADOWING")
	@Composable
	override fun Content() {
		val product = remember { RegistrationFeatureFactory.create<SmsCodeIntent, SmsCodeState>() }
		val intent = product.intent
		val state = product.state

		val model by state.stateFlow.collectAsState(initial = BaseMppState.StateModel.Initial)

		val codeState = remember { mutableStateOf(emptyString) }
		val timeLeftState = remember { mutableStateOf(0) }
		val errorState = remember { mutableStateOf(emptyString) }
		val triesCountState = remember { mutableStateOf(Constants.OTP_MAX_TRIES) }
		val bottomSheetState = remember { mutableStateOf<BottomSheetInfo?>(null) }

		when (val model = model) {
			is BaseMppState.StateModel.Initial -> {
				intent.startTimer(timeLeft, System.currentTimeMillis())
			}
			is BaseMppState.StateModel.Error -> {
				codeState.value = emptyString
				errorState.value = Constants.OTP_INVALID_ERROR_CODE
				if (triesCountState.value <= 0) {
					intent.pauseTimer()
					bottomSheetState.value = BottomSheetInfo(
						title = "Вы ввели код неверно несколько раз. Попробуйте запросить новый код",
						buttons = listOf(
							ButtonView.Primary(
								text = "Закрыть",
								onClickListener = ButtonView.OnClickListener.onClickListener {
									intent.startTimer(timeLeft, System.currentTimeMillis())
									bottomSheetState.value = null
								},
							)
						)
					)
				}
			}
			SmsCodeState.SmsCodeStateModel.Request -> {
				triesCountState.value = Constants.OTP_MAX_TRIES
				codeState.value = emptyString
				errorState.value = emptyString
			}
			is SmsCodeState.SmsCodeStateModel.TimeLeft -> {
				timeLeftState.value = model.timeLeft
			}
		}

		MainContainer(
			mainState = model,
			sheetInfo = bottomSheetState.value,
			scrollable = true,
			contentHorizontalAlignment = Alignment.Start,
		) {

			Spacer(modifier = Modifier.weight(1f))

			Column(
				modifier = Modifier
					.fillMaxWidth()
					.align(Alignment.CenterHorizontally),
			) {
				TitleTextField(text = "Введите код подтверждения")
				Text(
					modifier = Modifier.padding(top = Deps.Spacing.colElementMargin),
					text = "Вводя код из SMS вы подписываете оферту, " +
							"подтверждая свое согласие\nМы отправили SMS на номер:",
					fontSize = Headings.H5.sp,
					fontWeight = FontWeight.Medium,
					lineHeight = Deps.TextSize.lineHeight,
					color = ComposeColors.DescriptionGray
				)
				Text(
					modifier = Modifier.padding(top = Deps.Spacing.minPadding * 2),
					text = phoneFormatter(phoneNumber),
					fontSize = Headings.H5.sp,
					fontWeight = FontWeight.Bold,
				)
			}

			CodeInput(
				modifier = Modifier.padding(top = Deps.Spacing.standardMargin * 2),
				value = codeState.value,
				showValue = true,
				onValueChanged = {
					if (errorState.value != emptyString) {
						errorState.value = emptyString
					}
					if (triesCountState.value > 0) {
						codeState.value = it
					}

				},
				onInputCompleted = {
					if (triesCountState.value > 0) {
						triesCountState.value--
						intent.smsCodeEntered(phoneNumber, it, referenceId)
					}
				},
				withKeyboard = true,
				showKeyboardPermanently = true,
				isValid = (errorState.value != Constants.OTP_INVALID_ERROR_CODE),
			)

			if (errorState.value == Constants.OTP_INVALID_ERROR_CODE) {
				Row(
					modifier = Modifier.align(Alignment.CenterHorizontally)
				) {
					Text(
						modifier = Modifier
							.padding(vertical = Deps.Spacing.standardMargin),
						text = "Неверный Код. ",
						fontSize = Headings.H4.sp,
						fontWeight = FontWeight.Normal,
						color = ComposeColors.PrimaryRed
					)
					Text(
						modifier = Modifier
							.padding(vertical = Deps.Spacing.standardMargin),
						text = "Осталось попыток: ${triesCountState.value}",
						fontSize = Headings.H4.sp,
						fontWeight = FontWeight.Normal,
						color = ComposeColors.DescriptionGray
					)
				}
			}
			Spacer(modifier = Modifier.weight(2f))

			PrimaryButton(
				modifier = Modifier.fillMaxWidth(),
				text = buttonTextFormatter(timeLeftState.value),
				color = ComposeColors.Green,
				onClick = {
					intent.smsCodeRequest(phoneNumber, System.currentTimeMillis())
				},
				enabled = (timeLeftState.value == 0),
			)

		}
	}

	private fun phoneFormatter(phone: String): String {
		val builder = StringBuilder().apply {
			append(Constants.PHONE_NUMBER_CODE)
			for (i in phone.indices) {
				if (i == 0) append("(")
				append(phone[i])

				if (i == 2) append(") ")
				if (i % 2 == 0 && i != 8 && i > 2) append(" ")
			}
		}
		return builder.toString()
	}

	private fun buttonTextFormatter(time: Int): String {
		return when {
			time <= 0 -> "Отправить повторно"
			time <= 60 -> "Запросить через $time сек."
			else -> "Запросить через ${DateUtils.formatElapsedTime(time.toLong())}"
		}
	}

}