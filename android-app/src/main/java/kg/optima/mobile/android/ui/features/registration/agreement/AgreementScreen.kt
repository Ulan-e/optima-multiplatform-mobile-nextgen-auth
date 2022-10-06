package kg.optima.mobile.android.ui.features.registration.agreement

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.base.di.create
import kg.optima.mobile.base.presentation.BaseMppState
import kg.optima.mobile.android.ui.base.MainContainer
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.buttons.SecondaryButton
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonSecondaryType
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView
import kg.optima.mobile.design_system.android.ui.text_fields.AnnotatedText
import kg.optima.mobile.design_system.android.ui.text_fields.TitleTextField
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

	@OptIn(ExperimentalMaterialApi::class)
	@Composable
	override fun Content() {
		val product = remember {
			RegistrationFeatureFactory.create<AgreementIntent, AgreementState>()
		}
		val intent = product.intent
		val state = product.state

		val model by state.stateFlow.collectAsState(initial = BaseMppState.StateModel.Initial)

		val infoState = remember { mutableStateOf<BottomSheetInfo?>(null) }

		MainContainer(
			mainState = model,
			contentModifier = Modifier
				.fillMaxSize()
				.padding(all = Deps.Spacing.standardPadding),
			sheetInfo = infoState.value,
			onSheetStateChanged = { sheetState, onBack ->
				when (sheetState) {
					ModalBottomSheetValue.Hidden -> onBack()
					else -> Unit
				}
			},
			contentHorizontalAlignment = Alignment.Start,
		) { onBack ->
			Column(
				modifier = Modifier
					.weight(1f)
					.verticalScroll(rememberScrollState()),
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
						fontWeight = FontWeight.Medium,
						fontSize = Headings.H5.sp,
						color = ComposeColors.DarkGray,
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
					AnnotatedText(
						text = "А также согласны на обработку своих персональных данных, в соответствии \n" +
								"с Законом Кыргызской Республики \n" +
								"«Об информации персонального характера» для целей получения банковских услуг ,\n" +
								"и выполнения требований действующего законодательства КР \n и с условиями оферты",
						underLineText = "условиями оферты",
						onClick = { intent.openOfferta() },
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
				onClick = {
					infoState.value = BottomSheetInfo(
						title = "Вы можете проконсультироваться и зарегистрироваться в ближайшем отделении банка",
						buttons = listOf(
							ButtonView.Primary(
								text = "Отделение",
								onClickListener = ButtonView.onClickListener {
									// TODO
								}
							),
							ButtonView.Transparent(
								text = "Закрыть",
								onClickListener = ButtonView.onClickListener {
									infoState.value = null
									onBack()
								}
							)
						)
					)
//					intent.pop()
				}
			)
		}
	}
}