package kg.optima.mobile.android.ui.features.registration.secret_question

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.dropdown_list.DropDownItemModel
import kg.optima.mobile.design_system.android.ui.dropdown_list.DropDownItemsList
import kg.optima.mobile.design_system.android.ui.dropdown_list.DropDownList
import kg.optima.mobile.design_system.android.ui.input.InputField
import kg.optima.mobile.design_system.android.ui.text_fields.TitleTextField
import kg.optima.mobile.design_system.android.ui.toolbars.NavigationIcon
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarInfo
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.registration.RegistrationFeatureFactory
import kg.optima.mobile.registration.presentation.secret_question.SecretQuestionIntent
import kg.optima.mobile.registration.presentation.secret_question.SecretQuestionState
import kg.optima.mobile.registration.presentation.secret_question.model.Question
import kg.optima.mobile.resources.Headings

class ControlQuestionScreen(
	val hashCode : String
) : Screen {

	@OptIn(ExperimentalComposeUiApi::class)
	@Suppress("NAME_SHADOWING")
	@Composable
	override fun Content() {

		val product = remember { RegistrationFeatureFactory.create<SecretQuestionIntent, SecretQuestionState>() }
		val intent = product.intent
		val state = product.state

		val model by state.stateFlow.collectAsState(initial = State.StateModel.Initial)

		val list = listOf(
			Question("Сколько цифр в слове гвоздь", "4"),
			Question("Сколько цифр в слове болт", "4"),
			Question("Сколько цифр в слове слон", "4"),
			Question("Сколько цифр в слове выдра", "4"),
			Question("Сколько цифр в слове конь", "4")
		)
		val newList = mutableListOf<DropDownItemModel<Question>>()
		list.map {
			newList.add(DropDownItemModel(it.question, it))
		}

		val answerInputText = remember { mutableStateOf(emptyString) }
		val buttonEnabled = remember { mutableStateOf(false) }
		val dropDownExpandedState = remember { mutableStateOf(false) }
		val items = remember {
//			intent.getQuestions()
//			mutableStateOf(DropDownItemsList(list = listOf<DropDownItemModel<Question>>()))
			mutableStateOf(DropDownItemsList(newList))
		}

		val keyboardController = LocalSoftwareKeyboardController.current

		when (val model = model) {
			SecretQuestionState.SecretQuestionModel.ShowQuestions -> {
				dropDownExpandedState.value = true
			}
			SecretQuestionState.SecretQuestionModel.HideQuestions -> {
				dropDownExpandedState.value = false
			}
			is SecretQuestionState.SecretQuestionModel.SetQuestion -> {
				items.value = items.value.copy(
					selectedItemIndex = items.value.list.indexOf(
						DropDownItemModel(model.question.question, model.question)
					)
				)
				dropDownExpandedState.value = false
			}

			is SecretQuestionState.SecretQuestionModel.ValidateResult -> buttonEnabled.value =
				model.success
			is SecretQuestionState.SecretQuestionModel.GetQuestions -> {
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
				navigationIcon = NavigationIcon(onBackClick = { intent.pop() })
			),
			contentHorizontalAlignment = Alignment.Start,
		) {
			Column(
				modifier = Modifier
					.weight(1f)
					.fillMaxSize()
					.padding(all = Deps.Spacing.standardPadding),
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				TitleTextField(
					modifier = Modifier.padding(vertical = Deps.Spacing.numPadXMargin),
					text = "Выберите контрольный вопрос",
				)
				DropDownList(
					items = items.value,
					expanded = dropDownExpandedState.value,
					onItemSelected = {
						intent.setQuestion(it)
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
					modifier = Modifier
						.fillMaxWidth()
						.padding(
							top = Deps.Spacing.swiperTopMargin,
							bottom = Deps.Spacing.standardPadding
						),
				)
				InputField(
					hint = "Ответ",
					valueState = answerInputText,
					onValueChange = {
						intent.onValueChanged(it)
						answerInputText.value = it
					}
				)
			}

			PrimaryButton(
				modifier = Modifier
					.fillMaxWidth()
					.padding(Deps.Spacing.standardPadding),
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