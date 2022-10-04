package kg.optima.mobile.android.ui.features.registration.control_question

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.base.di.create
import kg.optima.mobile.base.presentation.BaseMppState
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.dropdown_list.DropDownItemModel
import kg.optima.mobile.design_system.android.ui.dropdown_list.DropDownItemsList
import kg.optima.mobile.design_system.android.ui.dropdown_list.DropDownList
import kg.optima.mobile.design_system.android.ui.input.InputField
import kg.optima.mobile.design_system.android.ui.text_fields.TitleTextField
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarInfo
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.registration.RegistrationFeatureFactory
import kg.optima.mobile.registration.presentation.control_question.ControlQuestionIntent
import kg.optima.mobile.registration.presentation.control_question.ControlQuestionState
import kg.optima.mobile.registration.presentation.control_question.model.Question
import kg.optima.mobile.resources.Headings

class ControlQuestionScreen(
    private val hashCode: String,
) : Screen {

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
    @Suppress("NAME_SHADOWING")
    @Composable
    override fun Content() {
        val product =
            remember { RegistrationFeatureFactory.create<ControlQuestionIntent, ControlQuestionState>() }
        val intent = product.intent
        val state = product.state

        val model by state.stateFlow.collectAsState(initial = BaseMppState.StateModel.Initial)

        val answerInputText = remember { mutableStateOf(emptyString) }
        val buttonEnabled = remember { mutableStateOf(false) }
        val questionChecked = remember { mutableStateOf(false) }
        val validationChecked = remember { mutableStateOf(false) }
        val dropDownExpandedState = remember { mutableStateOf(false) }
        val items = remember {
            intent.getQuestions()
            mutableStateOf(DropDownItemsList(list = listOf<DropDownItemModel<Question>>()))
        }
        val maxSize = 20

        val keyboardController = LocalSoftwareKeyboardController.current
        BackHandler(enabled = true, onBack = {})

        when (val model = model) {
            ControlQuestionState.ControlQuestionModel.ShowQuestions -> {
                dropDownExpandedState.value = true
            }
            ControlQuestionState.ControlQuestionModel.HideQuestions -> {
                dropDownExpandedState.value = false
            }
            is ControlQuestionState.ControlQuestionModel.SetQuestion -> {
                items.value = items.value.copy(
                    selectedItemIndex = items.value.list.indexOf(
                        DropDownItemModel(model.question.question, model.question)
                    )
                )
                dropDownExpandedState.value = false
                buttonEnabled.value = validationChecked.value && questionChecked.value
            }
            is ControlQuestionState.ControlQuestionModel.ValidateResult -> {
                validationChecked.value = model.success
                buttonEnabled.value = validationChecked.value && questionChecked.value
            }

            is ControlQuestionState.ControlQuestionModel.GetQuestions -> {
                val newList = mutableListOf<DropDownItemModel<Question>>()
                model.questions.map {
                    newList.add(DropDownItemModel(it.question, it))
                }
                items.value = DropDownItemsList(newList)
            }
        }

        MainContainer(
            mainState = model,
            toolbarInfo = ToolbarInfo(
                navigationIcon = null
            ),
            contentHorizontalAlignment = Alignment.Start,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            TitleTextField(
                modifier = Modifier.padding(vertical = Deps.Spacing.numPadXMargin),
                text = "Выберите контрольный вопрос",
            )
            DropDownList(
                items = items.value,
                expanded = dropDownExpandedState.value,
                onItemSelected = {
                    intent.setQuestion(it)
                    questionChecked.value = true
                },
                onExpandClick = { intent.showQuestions() },
                onDismiss = { intent.hideQuestions() },
                modifier = Modifier.fillMaxWidth(),
                keyboardController = keyboardController
            )
            Text(
                text = "Контрольный вопрос необходим \nдля подтверждения личности",
                color = ComposeColors.DescriptionGray,
                fontSize = Headings.H5.sp,
                lineHeight = 17.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = Deps.Spacing.swiperTopMargin,
                        bottom = Deps.Spacing.standardPadding
                    ),
            )
            InputField(
                keyboardType = KeyboardType.Email,
                maxLength = 20,
                hint = "Ответ",
                valueState = answerInputText,
                onValueChange = {
                    if (it.length <= maxSize) {
                        intent.onValueChanged(it)
                        answerInputText.value = it
                    }
                }
            )
            Spacer(modifier = Modifier.weight(2f))
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Продолжить",
                color = ComposeColors.Green,
                onClick = {
                    if (items.value.list.isNotEmpty()) {
                        intent.confirm(
                            hashCode = hashCode,
                            questionId = items.value.selectedItem!!.entity.questionId,
                            answer = answerInputText.value
                        )
                    }
                },
                enabled = buttonEnabled.value,
            )
        }
    }
}