package kg.optima.mobile.android.ui.features.registration

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.common.Constants
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.text_fields.PhoneNumberTextField
import kg.optima.mobile.design_system.android.ui.text_fields.TitleTextField
import kg.optima.mobile.design_system.android.ui.toolbars.NavigationIcon
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarInfo
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.Headings

object PhoneNumberScreen : Screen {

	@Composable
	override fun Content() {
		val navigator = LocalNavigator.currentOrThrow

		var phoneNumberState by remember { mutableStateOf(emptyString) }
		var buttonEnableState by remember { mutableStateOf(false) }

		MainContainer(
			mainState = null,
			toolbarInfo = ToolbarInfo(
				navigationIcon = NavigationIcon(onBackClick = { navigator.pop() })
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
				fontSize = Headings.H5.sp,
			)
			PhoneNumberTextField(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = Deps.Spacing.standardMargin * 2),
				phoneNumber = phoneNumberState,
				onValueChange = {
					if (it.length <= Constants.PHONE_NUMBER_LENGTH) {
						buttonEnableState = it.length == Constants.PHONE_NUMBER_LENGTH
						phoneNumberState = it
					}
				},
			)

			Spacer(modifier = Modifier.weight(1f))

			PrimaryButton(
				modifier = Modifier.fillMaxWidth(),
				text = "Продолжить",
				enabled = buttonEnableState,
				color = ComposeColors.Green,
				onClick = {

				}
			)
		}
	}
}
