package kg.optima.mobile.android.ui.features.registration.phone_number

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
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
import kg.optima.mobile.design_system.android.ui.text_fields.PhoneNumberTextField
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


object PhoneNumberScreen : Screen {

	@OptIn(ExperimentalMaterialApi::class)
	@Suppress("NAME_SHADOWING")
	@Composable
	override fun Content() {
		val product = RegistrationFeatureFactory.create<PhoneNumberIntent, PhoneNumberState>()
		val intent = product.intent
		val state = product.state

		val model by state.stateFlow.collectAsState(initial = State.StateModel.Initial)

		var phoneNumber by remember { mutableStateOf(emptyString) }
		var buttonEnabled by remember { mutableStateOf(false) }

		when (val model = model) {
			is PhoneNumberState.PhoneNumberStateModel.ValidateResult -> {
				buttonEnabled = model.success
			}
		}

		MainContainer(
			mainState = model,
			toolbarInfo = ToolbarInfo(
				navigationIcon = NavigationIcon(onBackClick = { intent.pop() })
			),
			contentHorizontalAlignment = Alignment.Start,
		) {
			TitleTextField(
				modifier = Modifier.padding(top = Deps.Spacing.bigMarginTop),
				text = "Введите номер телефона: ",
			)
			Text(
				modifier = Modifier.padding(top = Deps.Spacing.subheaderMargin),
				text = "Номер телефона будет использоваться для переводов, " +
						"и отправки SMS-кода для подтверждения некоторых операций",
				color = ComposeColors.DarkGray,
				fontSize = Headings.H5.sp,
				fontWeight = FontWeight.Medium,
			)
			PhoneNumberTextField(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = Deps.Spacing.standardMargin * 2),
				phoneNumber = phoneNumber,
				onValueChange = {
					intent.onValueChanged(it)
					phoneNumber = it
				},
			)

			Spacer(modifier = Modifier.weight(1f))

			PrimaryButton(
				modifier = Modifier.fillMaxWidth(),
				text = "Продолжить",
				enabled = buttonEnabled,
				color = ComposeColors.Green,
				onClick = { intent.phoneNumberEntered(phoneNumber) }
			)
		}
	}
}
