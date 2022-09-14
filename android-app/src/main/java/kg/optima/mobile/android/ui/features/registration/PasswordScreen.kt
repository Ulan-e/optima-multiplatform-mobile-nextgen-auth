package kg.optima.mobile.android.ui.features.registration

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.input.PasswordInput
import kg.optima.mobile.design_system.android.ui.input.model.ErrorTextField
import kg.optima.mobile.design_system.android.ui.password_validity.PasswordValidityList
import kg.optima.mobile.design_system.android.ui.text_fields.TitleTextField
import kg.optima.mobile.design_system.android.ui.toolbars.NavigationIcon
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarInfo
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.registration.RegistrationFeatureFactory
import kg.optima.mobile.registration.presentation.password.PasswordValidator
import kg.optima.mobile.registration.presentation.password.PasswordValidityModel
import kg.optima.mobile.registration.presentation.phone_number.PhoneNumberIntent
import kg.optima.mobile.registration.presentation.phone_number.PhoneNumberState
import kg.optima.mobile.resources.Headings

class PasswordScreen(
	val hashCode: String,
	val questionId : String,
	val answer: String
) : Screen {

	@Composable
	override fun Content() {

		//TODO: create own state/intent
		val product = RegistrationFeatureFactory.create<PhoneNumberIntent, PhoneNumberState>()
		val intent = product.intent
		val state = product.state

		val model by state.stateFlow.collectAsState(initial = State.StateModel.Initial)

		val buttonEnabled = remember { mutableStateOf(false) }
		val passwordValidity = remember { mutableStateOf(PasswordValidityModel.BASIC_VALIDITY) }
		val passwordState = remember { mutableStateOf(emptyString) }
		val rePasswordState = remember { mutableStateOf(emptyString) }
		val errorState = remember { mutableStateOf(ErrorTextField.empty()) }

		MainContainer(
			mainState = model,
			toolbarInfo = ToolbarInfo(
				navigationIcon = NavigationIcon(onBackClick = { intent.pop() })
			),
			contentHorizontalAlignment = Alignment.Start,
		) {

			TitleTextField(
				text = "Создание пароля",
				modifier = Modifier.padding(top = Deps.Spacing.bigMarginTop)
			)
			PasswordInput(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = Deps.Spacing.spacing),
				passwordState = passwordState,
				hint = "Пароль",
				onValueChange = {
					passwordValidity.value = PasswordValidator.validate(it)
				},
				errorState = errorState
			)
			Text(
				text = "Пароль для входа в приложение",
				color = ComposeColors.DescriptionGray,
				fontSize = Headings.H5.sp,
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						top = Deps.Spacing.swiperTopMargin,
						bottom = Deps.Spacing.standardPadding
					),
			)
			PasswordInput(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = Deps.Spacing.spacing),
				passwordState = rePasswordState,
				onValueChange = {
					//TODO: callback
				},
				hint = "Повторить пароль"
			)
			Text(
				text = "Пароль должен содержать:",
				fontSize = Headings.H4.sp,
				color = ComposeColors.DescriptionGray,
				fontWeight = FontWeight.Bold,
				modifier = Modifier.padding(
					top = Deps.Spacing.standardMargin * 2,
					bottom = Deps.Spacing.standardMargin
				)
			)

			PasswordValidityList(list = passwordValidity.value)

			Spacer(modifier = Modifier.weight(1f, fill = true))

			PrimaryButton(
				modifier = Modifier
					.fillMaxWidth()
					.padding(Deps.Spacing.standardPadding),
				text = "Продолжить",
				color = ComposeColors.Green,
				onClick = {  },
				enabled = buttonEnabled.value,
			)

		}
	}

}