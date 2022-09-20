package kg.optima.mobile.android.ui.features.registration.create_password

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView
import kg.optima.mobile.design_system.android.ui.input.model.ErrorTextField
import kg.optima.mobile.design_system.android.ui.password_validity.PasswordValidityList
import kg.optima.mobile.design_system.android.ui.text_fields.TitleTextField
import kg.optima.mobile.design_system.android.ui.toolbars.NavigationIcon
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarInfo
import kg.optima.mobile.design_system.android.utils.resources.ComposeColor
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.registration.RegistrationFeatureFactory
import kg.optima.mobile.registration.presentation.create_password.CreatePasswordIntent
import kg.optima.mobile.registration.presentation.create_password.CreatePasswordState
import kg.optima.mobile.registration.presentation.create_password.validity.PasswordValidator
import kg.optima.mobile.registration.presentation.create_password.validity.PasswordValidityModel
import kg.optima.mobile.resources.Headings

class CreatePasswordScreen(
    val hash: String,
    val questionId: String,
    val answer: String,
) : Screen {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val product = remember {
            RegistrationFeatureFactory.create<CreatePasswordIntent, CreatePasswordState>()
        }
        val intent = product.intent
        val state = product.state

        val model by state.stateFlow.collectAsState(initial = State.StateModel.Initial)

        val buttonEnabled = remember { mutableStateOf(false) }
        val passwordValidity = remember { mutableStateOf(PasswordValidityModel.BASIC_VALIDITY) }
        val passwordState = remember { mutableStateOf(emptyString) }
        val rePasswordState = remember { mutableStateOf(emptyString) }
        val errorState = remember { mutableStateOf(ErrorTextField.empty()) }

        val outlineColor = remember { mutableStateOf(Color.Transparent) }
        val outlineColor2 = remember { mutableStateOf(Color.Transparent) }

        val bottomSheetState = remember { mutableStateOf<BottomSheetInfo?>(null) }

        when (val createPasswordStateModel = model) {
            is CreatePasswordState.CreatePasswordStateModel.ValidationResult -> {
                passwordValidity.value = createPasswordStateModel.validityModels
            }
            is CreatePasswordState.CreatePasswordStateModel.ComparisonResult -> {
                buttonEnabled.value = createPasswordStateModel.matches
            }
        }

        LaunchedEffect(key1 = buttonEnabled.value) {
            if (buttonEnabled.value) {
                outlineColor2.value = ComposeColors.Green
            } else {
                outlineColor2.value = Color.Transparent
            }
        }

        MainContainer(
            mainState = model,
            sheetInfo = bottomSheetState.value,
            toolbarInfo = ToolbarInfo(
                navigationIcon = NavigationIcon(onBackClick = { intent.pop() })
            ),
            scrollable = true,
            contentHorizontalAlignment = Alignment.Start,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            TitleTextField(text = "Создание пароля")
            PasswordOutlineInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Deps.Spacing.spacing),
                passwordState = passwordState,
                hint = "Пароль",
                onValueChange = {
                    passwordValidity.value = PasswordValidator.validate(it)
                    intent.validate(it)

                    if (
                        passwordValidity.value.first().isValid &&
                        passwordValidity.value[1].isValid &&
                        passwordValidity.value.last().isValid
                    ) {
                        outlineColor.value = ComposeColors.Green
                        buttonEnabled.value = passwordState.value == rePasswordState.value
                    } else {
                        outlineColor.value = Color.Transparent
                        buttonEnabled.value = false
                    }

                },
                errorState = errorState,
                outlineColor = outlineColor.value
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Deps.Spacing.swiperTopMargin),
                text = "Пароль для входа в приложение",
                color = ComposeColors.DescriptionGray,
                fontSize = Headings.H5.sp,
            )
            PasswordOutlineInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Deps.Spacing.spacing),
                passwordState = rePasswordState,
                onValueChange = {
                    if (
                        passwordValidity.value.first().isValid &&
                        passwordValidity.value[1].isValid &&
                        passwordValidity.value.last().isValid
                    ) {
                        buttonEnabled.value = passwordState.value == rePasswordState.value
                    } else {
                        buttonEnabled.value = false
                    }
                },
                hint = "Повторить пароль",
                errorState = errorState,
                outlineColor = outlineColor2.value
            )
            Text(
                modifier = Modifier.padding(
                    top = Deps.Spacing.standardMargin * 2,
                    bottom = Deps.Spacing.standardMargin
                ),
                text = "Пароль должен содержать:",
                fontSize = Headings.H4.sp,
                color = ComposeColors.DescriptionGray,
                fontWeight = FontWeight.Bold,
            )
            PasswordValidityList(list = passwordValidity.value)
            Spacer(modifier = Modifier.weight(2f))
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Продолжить",
                color = ComposeColors.Green,
                enabled = buttonEnabled.value,
                onClick = {
                    /*intent.submit(
                        hash = hash,
                        password = passwordState.value,
                        questionId = questionId,
                        answer = answer
                    )*/
                    bottomSheetState.value = BottomSheetInfo(
                        title = "Поздравляем! \nВы зарегистрированы в Optima24",
                        composableContent = BottomSheetInfo.ComposableContent.composableContent {
                            Text(
                                modifier = Modifier.padding(top = Deps.Spacing.standardMargin),
                                text = "Ваш Client ID",
                                fontSize = Headings.H4.px.sp,
                                color = ComposeColors.PrimaryBlack,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                modifier = Modifier.padding(top = 6.dp),
                                text = "907490",
                                fontSize = 34.sp,
                                color = ComposeColors.Green,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                modifier = Modifier.padding(top = 12.dp),
                                text = "Запомните его. Он является вашим \nлогином для входа \nв \"Optima24\"",
                                fontSize = Headings.H4.px.sp,
                                textAlign = TextAlign.Center
                            )
                        },
                        buttons = listOf(
                            ButtonView.Primary(
                                text = "Готово",
                                onClickListener = ButtonView.OnClickListener.onClickListener {
                                    intent.submit(
                                        hash = hash,
                                        password = passwordState.value,
                                        questionId = questionId,
                                        answer = answer
                                    )
                                },
                                composeColor = ComposeColor.composeColor(ComposeColors.Green)
                            )
                        )
                    )
                }
            )
        }
    }
}