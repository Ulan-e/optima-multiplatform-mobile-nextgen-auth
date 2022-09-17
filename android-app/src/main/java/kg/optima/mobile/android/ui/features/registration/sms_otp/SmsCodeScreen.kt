package kg.optima.mobile.android.ui.features.registration.sms_otp

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.core.common.Constants
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.input.CodeInput
import kg.optima.mobile.design_system.android.ui.text_fields.TitleTextField
import kg.optima.mobile.design_system.android.ui.text_fields.phoneTextFormatter
import kg.optima.mobile.design_system.android.ui.toolbars.NavigationIcon
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarInfo
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.registration.RegistrationFeatureFactory
import kg.optima.mobile.registration.presentation.sms_code.SmsCodeIntent
import kg.optima.mobile.registration.presentation.sms_code.SmsCodeState
import kg.optima.mobile.resources.Headings

@Parcelize
class SmsCodeScreen(
    private val phoneNumber: String,
    private val timeout: Int,
    private val referenceId: String,
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
        val timeoutState = remember { intent.startTimeout(timeout); mutableStateOf(timeout) }
        val errorState = remember { mutableStateOf(emptyString) }
        val triesCountState = remember { mutableStateOf(0) }

        when (val model = model) {
            is SmsCodeState.SmsCodeStateModel.InvalidCodeError -> {
                errorState.value = model.error
            }
            is SmsCodeState.SmsCodeStateModel.EnableReRequest -> {
                buttonIsEnabledState.value = model.enabled
            }
            is SmsCodeState.SmsCodeStateModel.Timeout -> {
                timeoutState.value = model.timeout
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
					.padding(top = Deps.Spacing.bigMarginTop)
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
                    text = phoneFormatter(phoneNumber),
                    fontSize = Headings.H2.sp,
                    fontWeight = FontWeight.Normal,
                )
            }

            CodeInput(
                modifier = Modifier.padding(top = Deps.Spacing.standardMargin * 2),
                value = codeState.value,
                showValue = true,
                onValueChanged = { codeState.value = it; errorState.value = emptyString },
                onInputCompleted = { intent.smsCodeEntered(phoneNumber, it, referenceId) },
                withKeyboard = true,
                isValid = (errorState.value == ""),
            )

            if (errorState.value.isNotEmpty()) {
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
            Spacer(modifier = Modifier.weight(1f))
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = buttonTextFormatter(timeoutState.value),
                color = ComposeColors.Green,
                onClick = { intent.smsCodeReRequest(timeout, phoneNumber) },
                enabled = buttonIsEnabledState.value,
            )

        }
    }

    private fun phoneFormatter(phone : String) : String {
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

    private fun buttonTextFormatter(time: Int) : String {
        return when {
            time == 0 -> "Отправить повторно"
            time <= 60 -> "Запросить через $time сек."
            time in 61..3599 -> "Запросить через ${time/60}:${time % 60}"
            else -> "Запросить через ${time/3600}:${time/60}:${time % 60}"
        }
    }

}