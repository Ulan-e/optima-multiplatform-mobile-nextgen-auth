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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.base.MainContainer
import kg.optima.mobile.android.utils.navigateTo
import kg.optima.mobile.base.di.create
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView
import kg.optima.mobile.design_system.android.ui.input.model.ErrorTextField
import kg.optima.mobile.design_system.android.ui.password_validity.PasswordValidityList
import kg.optima.mobile.design_system.android.ui.text_fields.TitleTextField
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
	private val createPasswordModel: CreatePasswordModel
) : Screen {

	@OptIn(ExperimentalMaterialApi::class)
	@Composable
	override fun Content() {
		val product = remember {
			RegistrationFeatureFactory.create<CreatePasswordIntent, CreatePasswordState>()
		}
		val intent = product.intent
		val state = product.state

		val model by state.stateFlow.collectAsState(initial = UiState.Model.Initial)

		val buttonEnabled = remember { mutableStateOf(false) }
		val passwordValidity = remember { mutableStateOf(PasswordValidityModel.BASIC_VALIDITY) }
		val passwordState = remember { mutableStateOf(emptyString) }
		val rePasswordState = remember { mutableStateOf(emptyString) }
		val errorState = remember { mutableStateOf(ErrorTextField.empty()) }

		val outlineColor = remember { mutableStateOf(Color.Transparent) }
		val outlineColor2 = remember { mutableStateOf(Color.Transparent) }

		val bottomSheetState = remember { mutableStateOf<BottomSheetInfo?>(null) }

		val context = LocalContext.current
		val focusManager = LocalFocusManager.current

		LaunchedEffect(key1 = buttonEnabled) { focusManager.clearFocus() }

		when (val createPasswordStateModel: UiState.Model? = model) {
			is CreatePasswordState.Model.ValidationResult -> {
				passwordValidity.value = createPasswordStateModel.validityModels
			}
			is CreatePasswordState.Model.ComparisonResult -> {
				buttonEnabled.value = createPasswordStateModel.matches
			}
			is CreatePasswordState.Model.RegisterSuccessResult -> {
				intent.init()
				bottomSheetState.value = BottomSheetInfo(
					title = createPasswordStateModel.message,
					composableContent = BottomSheetInfo.ComposableContent.composableContent {
						Text(
							modifier = Modifier.padding(top = Deps.Spacing.standardMargin),
							text = "??????????????????????! ???? ???????????????????????????????? ?? Optima24",
							fontSize = Headings.H2.px.sp,
							color = ComposeColors.PrimaryBlack,
							fontWeight = FontWeight.Bold,
							textAlign = TextAlign.Center
						)
						Text(
							modifier = Modifier.padding(top = Deps.Spacing.standardMargin),
							text = "?????? Client ID",
							fontSize = Headings.H4.px.sp,
							color = ComposeColors.PrimaryBlack,
							fontWeight = FontWeight.Bold
						)
						Text(
							modifier = Modifier.padding(top = 6.dp),
							text = createPasswordStateModel.clientId ?: "",
							fontSize = 34.sp,
							color = ComposeColors.Green,
							fontWeight = FontWeight.Bold
						)
						Text(
							modifier = Modifier.padding(top = 12.dp),
							text = "?????????????????? ??????. ???? ???????????????? ?????????? \n?????????????? ?????? ?????????? \n?? \"Optima24\"",
							fontSize = Headings.H4.px.sp,
							textAlign = TextAlign.Center
						)
					},
					buttons = listOf(
						ButtonView.Primary(
							text = "????????????",
							onClickListener = ButtonView.onClickListener {
								intent.onRegistrationDone()
							},
							composeColor = ComposeColor.composeColor(ComposeColors.Green)
						)
					)
				)
			}
			is CreatePasswordState.Model.RegisterFailedResult -> {
				intent.init()
				bottomSheetState.value = BottomSheetInfo(
					title = createPasswordStateModel.message,
					buttons = listOf(
						ButtonView.Primary(
							text = "???? ??????????????",
							composeColor = ComposeColor.composeColor(ComposeColors.PrimaryRed),
							onClickListener = ButtonView.onClickListener(intent::returnToMain),
						)
					)
				)
			}
			is CreatePasswordState.Model.NavigateTo.Welcome ->
				context.navigateTo(createPasswordStateModel)
		}

		MainContainer(
			mainState = model,
			sheetInfo = bottomSheetState.value,
			scrollable = true,
			contentHorizontalAlignment = Alignment.Start,
		) {
			Spacer(modifier = Modifier.weight(1f))
			TitleTextField(text = "???????????????? ????????????")
			PasswordOutlineInput(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = Deps.Spacing.spacing),
				passwordState = passwordState,
				hint = "????????????",
				onValueChange = {
					passwordValidity.value = PasswordValidator.validate(it, rePasswordState.value)
					intent.validate(it, rePasswordState.value)
					if (passwordValidity.value.last().isValid) {
						outlineColor2.value = ComposeColors.Green
						buttonEnabled.value = true
					} else if (
						passwordValidity.value.first().isValid &&
						passwordValidity.value[1].isValid &&
						passwordValidity.value[2].isValid
					) {
						buttonEnabled.value = false
						outlineColor.value = ComposeColors.Green
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
				text = "???????????? ?????? ?????????? ?? ????????????????????",
				color = ComposeColors.DescriptionGray,
				fontSize = Headings.H5.sp,
			)
			PasswordOutlineInput(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = Deps.Spacing.spacing),
				passwordState = rePasswordState,
				onValueChange = {
					passwordValidity.value = PasswordValidator.validate(passwordState.value, it)
					intent.compare(passwordState.value, it)
					if (passwordState.value != it && it.isNotEmpty()) {
						outlineColor2.value = ComposeColors.PrimaryRed
						buttonEnabled.value = false
					} else if (
						passwordValidity.value.first().isValid &&
						passwordValidity.value[1].isValid &&
						passwordValidity.value[2].isValid &&
						passwordValidity.value.last().isValid
					) {
						outlineColor2.value = ComposeColors.Green
						buttonEnabled.value = true
					} else {
						outlineColor2.value = Color.Transparent
						buttonEnabled.value = false
					}
				},
				hint = "?????????????????? ????????????",
				errorState = errorState,
				outlineColor = outlineColor2.value
			)
			Text(
				modifier = Modifier.padding(
					top = Deps.Spacing.standardMargin * 2,
					bottom = Deps.Spacing.standardMargin
				),
				text = "???????????? ???????????? ??????????????????:",
				fontSize = Headings.H4.sp,
				color = ComposeColors.DescriptionGray,
				fontWeight = FontWeight.Bold,
			)
			PasswordValidityList(list = passwordValidity.value)
			Spacer(modifier = Modifier.weight(2f))
			PrimaryButton(
				modifier = Modifier.fillMaxWidth(),
				text = "????????????????????",
				color = ComposeColors.Green,
				enabled = buttonEnabled.value,
				onClick = {
					intent.register(
						hash = createPasswordModel.hash,
						password = passwordState.value,
						questionId = createPasswordModel.questionId,
						answer = createPasswordModel.answer,
					)
				}
			)
		}
	}
}