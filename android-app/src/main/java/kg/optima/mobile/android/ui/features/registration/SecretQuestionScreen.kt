package kg.optima.mobile.android.ui.features.registration

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
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.registration.RegistrationFeatureFactory
import kg.optima.mobile.registration.presentation.T
import kg.optima.mobile.registration.presentation.phone_number.PhoneNumberIntent
import kg.optima.mobile.registration.presentation.phone_number.PhoneNumberState

object SecretQuestionScreen : Screen {

	@OptIn(ExperimentalComposeUiApi::class)
	@Suppress("NAME_SHADOWING")
	@Composable
	override fun Content() {

		//TODO: make own intent/state
		val product =
			remember { RegistrationFeatureFactory.create<PhoneNumberIntent, PhoneNumberState>() }
		val intent = product.intent
		val state = product.state

		val model by state.stateFlow.collectAsState(initial = State.StateModel.Initial)

		val inputFieldText = remember { mutableStateOf(emptyString) }
		val buttonEnabled = remember { mutableStateOf(false) }
		val dropDownExpandedState = remember { mutableStateOf(false) }
		val items = remember {
			mutableStateOf(
				DropDownItemsList(
					list = listOf(
						DropDownItemModel("Какие 5 последних цифр вашей кредитной карты?", T()),
						DropDownItemModel("Как звали вашего лучшего друга детства?", T()),
						DropDownItemModel("Какое имя вашей бабушки? ", T()),
						DropDownItemModel("Какая кличка вашего животного?", T()),
						DropDownItemModel("Какой ваш любимый фильм?", T()),
						DropDownItemModel("Ваш любимый цвет?", T()),
					),
					selectedItemIndex = 0
				)
			)
		}

		val keyboardController = LocalSoftwareKeyboardController.current
		val answerMinLength = 5



		buttonEnabled.value = inputFieldText.value.length > answerMinLength

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
						items.value = it
						dropDownExpandedState.value = false
					},
					onExpandClick = { dropDownExpandedState.value = true },
					onDismiss = { dropDownExpandedState.value = false },
					modifier = Modifier.fillMaxWidth(),
					keyboardController = keyboardController
				)
				Text(
					text = "Контрольный вопрос необходим \nдля подтверждения личности",
					color = ComposeColors.DescriptionGray,
					fontSize = 14.sp,
					modifier = Modifier
						.fillMaxWidth()
						.padding(
							top = Deps.Spacing.swiperTopMargin,
							bottom = Deps.Spacing.standardPadding
						),
				)
				InputField(
					hint = "Ответ",
					valueState = inputFieldText,
				)
			}
			PrimaryButton(
				modifier = Modifier
					.fillMaxWidth()
					.padding(Deps.Spacing.standardPadding),
				text = "Продолжить",
				color = ComposeColors.Green,
				onClick = { },
				enabled = buttonEnabled.value,
			)
		}
	}

}