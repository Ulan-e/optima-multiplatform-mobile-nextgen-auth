package kg.optima.mobile.android.ui.features.registration.agreement

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.buttons.SecondaryButton
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonSecondaryType
import kg.optima.mobile.design_system.android.ui.text_fields.TitleTextField
import kg.optima.mobile.design_system.android.ui.toolbars.NavigationIcon
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarInfo
import kg.optima.mobile.design_system.android.utils.resources.ComposeColor
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.resId
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.registration.RegistrationFeatureFactory
import kg.optima.mobile.registration.presentation.agreement.AgreementIntent
import kg.optima.mobile.registration.presentation.agreement.AgreementState
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.images.RegistrationImages

@Parcelize
object AgreementScreen : BaseScreen {

	@Composable
	override fun Content() {
		val product = remember {
			RegistrationFeatureFactory.create<AgreementIntent, AgreementState>()
		}
		val intent = product.intent
		val state = product.state

		val model by state.stateFlow.collectAsState(initial = State.StateModel.Initial)

		MainContainer(
			mainState = model,
			contentModifier = Modifier.fillMaxSize(),
			toolbarInfo = ToolbarInfo(
				navigationIcon = NavigationIcon(onBackClick = { intent.pop() })
			),
			contentHorizontalAlignment = Alignment.Start,
		) {
			Column(
				modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
				verticalArrangement = Arrangement.Center
			) {
				TitleTextField(text = "Обратите внимание!")
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(
							top = Deps.Spacing.standardMargin * 2,
							bottom = Deps.Spacing.spacing
						)
				) {
					Image(
						modifier = Modifier.size(size = Deps.Size.buttonHeight),
						painter = painterResource(id = RegistrationImages.agreementInfoFirst.resId()),
						contentDescription = emptyString,
					)
					Text(
						modifier = Modifier.padding(start = Deps.Spacing.standardMargin),
						text = "Убедитесь, что Вы являетесь резидентом КР с оригинальным паспортом - " +
								"ID карта образца 2014 и 2017 года",
						fontSize = Headings.H4.sp,
						color = ComposeColors.PrimaryBlack,
					)
				}
				Row(
					modifier = Modifier.fillMaxWidth(),
				) {
					Image(
						modifier = Modifier.size(size = Deps.Size.buttonHeight),
						painter = painterResource(id = RegistrationImages.agreementInfoSecond.resId()),
						contentDescription = emptyString,
					)
					Text(
						modifier = Modifier.padding(start = Deps.Spacing.standardMargin),
						text = "<p>А также согласны на обработку своих персональных данных, " +
								"в соответствии с Законом Кыргызской Республики " +
								"«Об информации персонального характера» " +
								"для целей получения банковских услуг и выполнения требований " +
								"действующего законодательства КР и с " +
								"<link title=\"условиями оферты\" href=\"abc\">.</p>"
					)
				}
			}
			Text(
				fontSize = Headings.H5.sp,
				color = ComposeColors.DescriptionGray,
				text = "Нажимая на кнопку \"Согласен\", подтверждаю, " +
						"что ознакомлен и согласен с условиями договора",
			)
			PrimaryButton(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = Deps.Spacing.standardMargin),
				text = "Согласен",
				color = ComposeColors.Green,
				onClick = { intent.confirm() }
			)
			SecondaryButton(
				modifier = Modifier.fillMaxWidth(),
				text = "Не согласен",
				buttonType = ButtonSecondaryType.Main(
					composeColor = ComposeColor.composeColor(ComposeColors.PrimaryBlack)
				),
				onClick = { intent.pop() }
			)
		}
	}
}