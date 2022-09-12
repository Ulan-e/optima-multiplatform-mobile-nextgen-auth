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
import kg.optima.mobile.registration.presentation.phone_number.PhoneNumberIntent
import kg.optima.mobile.registration.presentation.phone_number.PhoneNumberState
import kg.optima.mobile.resources.Headings
import kotlinx.coroutines.delay

class SmsOtpScreen(
    private val phoneNumber: String,
    private val timeout: Int,
) : BaseScreen {


    @Suppress("NAME_SHADOWING")
    @Composable
    override fun Content() {

        //TODO: create own intent and state
        val product = RegistrationFeatureFactory.create<PhoneNumberIntent, PhoneNumberState>()
        val intent = product.intent
        val state = product.state

        val model by state.stateFlow.collectAsState(initial = State.StateModel.Initial)

        //TODO: get number from state
        val codeState = remember { mutableStateOf(emptyString) }
        val buttonIsEnabledState = remember { mutableStateOf(false) }
        val timeoutState = remember { mutableStateOf(timeout) }
        //TODO: remove mockup or set up with constants
        val buttonText = remember { mutableStateOf("Запросить через ${timeoutState.value} сек.") }
        val errorState = remember { mutableStateOf(emptyString) }

        LaunchedEffect(Unit) {
            while (timeoutState.value > 0) {
                delay(1000)
                timeoutState.value--
                buttonText.value = "Запросить через ${timeoutState.value} сек."
            }
            buttonText.value = "Отправить повторно"
            buttonIsEnabledState.value = true
        }

        MainContainer(
            mainState = null,
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
                SimpleGreyTextField(
                    modifier = Modifier.padding(top = Deps.Spacing.colElementMargin),
                    text = "Вводя код из SMS вы подписываете оферту, " +
                            "подтверждая свое согласие\nМы отправили SMS на номер:"
                )
                SimpleBlackTextField(text = phoneNumber)
            }

            CodeInput(
                modifier = Modifier.padding(top = Deps.Spacing.standardMargin * 2),
                value = codeState.value,
                onValueChanged = { codeState.value = it; { } },
                //TODO: callback onValueChanged
                onInputCompleted = { errorState.value = "" },
                //TODO: callback onInputCompleted
                withKeyboard = true,
                isValid = (errorState.value != "ERROR"),
                //TODO: remove mockup
            )

            if (errorState.value.isNotEmpty()) {
                ErrorTextField(
                    modifier = Modifier.padding(Deps.Spacing.standardMargin),
                    text = "Неверный Код."
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = buttonText.value,
                color = ComposeColors.Green,
                onClick = { errorState.value = "ERROR" },
                //TODO: callback onRetryClick
                enabled = buttonIsEnabledState.value,
            )

        }
    }

    @Composable
    private fun ErrorTextField(
        modifier: Modifier = Modifier,
        text: String,
    ) {
        Text(
            modifier = modifier,
            text = text,
            fontSize = Headings.H4.sp,
            fontWeight = FontWeight.Normal,
            color = ComposeColors.PrimaryRed
        )
    }

    @Composable
    private fun SimpleGreyTextField(
        modifier: Modifier = Modifier,
        text: String,
    ) {
        Text(
            modifier = modifier,
            text = text,
            fontSize = Headings.H4.sp,
            fontWeight = FontWeight.Normal,
            color = ComposeColors.DescriptionGray
        )
    }

    @Composable
    private fun SimpleBlackTextField(
        modifier: Modifier = Modifier,
        text: String,
    ) {
        Text(
            modifier = modifier,
            text = text,
            fontSize = Headings.H2.sp,
            fontWeight = FontWeight.Normal,
        )
    }

}