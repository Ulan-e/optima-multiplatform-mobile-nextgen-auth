package kg.optima.mobile.android.ui.features.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.input.CodeInput
import kg.optima.mobile.design_system.android.ui.text_fields.TitleTextField
import kg.optima.mobile.design_system.android.ui.toolbars.NavigationIcon
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarInfo
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.registration.RegistrationFeatureFactory
import kg.optima.mobile.registration.presentation.sms_code.SmsCodeIntent
import kg.optima.mobile.registration.presentation.sms_code.SmsCodeState
import kg.optima.mobile.resources.Headings

class SmsOtpScreen(
	private val phoneNumber: String,
	private val timeout: Int,
) : BaseScreen {


	@Suppress("NAME_SHADOWING")
	@Composable
	override fun Content() {

		val product = remember { RegistrationFeatureFactory.create<SmsCodeIntent, SmsCodeState>() }
		val intent = product.intent
		val state = product.state

		val model by state.stateFlow.collectAsState(initial = State.StateModel.Initial)

		val codeState = remember { mutableStateOf(emptyString) }
		val buttonIsEnabledState = remember { mutableStateOf(false) }
		val timeoutState = remember {
			intent.startTimeout()
			mutableStateOf(timeout)
		}
		val buttonTextState =
			remember { mutableStateOf(emptyString) }
		val errorState = remember { mutableStateOf(emptyString) }


		when (val model = model) {
			is SmsCodeState.SmsCodeStateModel.InvalidCodeError -> {
				errorState.value = model.error
			}
			is SmsCodeState.SmsCodeStateModel.EnableReRequest -> {
				buttonIsEnabledState.value = model.enabled
				buttonTextState.value = "Запросить повторно"
			}
			is SmsCodeState.SmsCodeStateModel.Timeout -> {
				timeoutState.value = model.timeout
				buttonTextState.value = "Запросить через ${timeoutState.value} сек."
			}
		}

		MainContainer(
			mainState = model,
			toolbarInfo = ToolbarInfo(
				navigationIcon = NavigationIcon(onBackClick = { intent.pop() })
			),
			contentHorizontalAlignment = Alignment.Start,
		) {

			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(Deps.Spacing.standardPadding, top = Deps.Spacing.bigMarginTop)
					.align(Alignment.CenterHorizontally),
			) {
				TitleTextField(text = "Введите код подтверждения")
				Text(
					modifier = Modifier.padding(top = Deps.Spacing.colElementMargin),
					text = "Вводя код из SMS вы подписываете оферту, " +
							"подтверждая свое согласие\nМы отправили SMS на номер:",
					fontSize = Headings.H4.sp,
					fontWeight = FontWeight.Normal,
					color = ComposeColors.DescriptionGray
				)
				Text(
					text = phoneNumber,
					fontSize = Headings.H2.sp,
					fontWeight = FontWeight.Normal,
				)
			}

			CodeInput(
				modifier = Modifier.padding(top = Deps.Spacing.standardMargin * 2),
				value = codeState.value,
				onValueChanged = {
					codeState.value = it
					errorState.value = emptyString
				},
				onInputCompleted = {
					intent.smsCodeEntered(phoneNumber, it)
				},
				withKeyboard = true,
				isValid = (errorState.value == ""),
			)

			if (errorState.value.isNotEmpty()) {
				Text(
					modifier = Modifier
						.align(Alignment.CenterHorizontally)
						.padding(Deps.Spacing.standardMargin),
					text = "Неверный Код.",
					fontSize = Headings.H4.sp,
					fontWeight = FontWeight.Normal,
					color = ComposeColors.PrimaryRed
				)
			}

			Spacer(modifier = Modifier.weight(1f))

			PrimaryButton(
				modifier = Modifier
					.fillMaxWidth(),
				text = "confirm",
				color = ComposeColors.Green,
				onClick = {
					intent.smsCodeEntered(phoneNumber, codeState.value)
				},
				enabled = true,
			)

			PrimaryButton(
				modifier = Modifier
					.fillMaxWidth(),
				text = buttonTextState.value,
				color = ComposeColors.Green,
				onClick = {
					intent.smsCodeReRequest(phoneNumber)
				},
				enabled = buttonIsEnabledState.value,
			)

		}
	}


}