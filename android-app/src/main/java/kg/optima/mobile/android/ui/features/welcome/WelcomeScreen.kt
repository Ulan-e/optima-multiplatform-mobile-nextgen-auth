package kg.optima.mobile.android.ui.features.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.android.utils.appVersion
import kg.optima.mobile.common.CommonFeatureFactory
import kg.optima.mobile.common.presentation.welcome.WelcomeIntent
import kg.optima.mobile.common.presentation.welcome.WelcomeState
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.buttons.TransparentButton
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.resId
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.images.MainImages


object WelcomeScreen : BaseScreen {

	@Composable
	override fun Content() {
		val product = remember {
			CommonFeatureFactory.create<WelcomeIntent, WelcomeState>()
		}
		val state = product.state
		val intent = product.intent

		val model by state.stateFlow.collectAsState(initial = null)

		val bottomSheetState = remember { mutableStateOf<BottomSheetInfo?>(null) }

		MainContainer(
			mainState = model,
			infoState = bottomSheetState.value,
		) {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(all = Deps.Spacing.standardPadding),
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				Image(
					modifier = Modifier
						.padding(top = 60.dp)
						.size(width = 170.dp, height = 36.dp),
					painter = painterResource(
						id = MainImages.optimaLogo.resId()
					),
					contentDescription = "Optima24",
				)
				WelcomeScreenButtonBlock(
					modifier = Modifier.weight(1f),
				)
				PrimaryButton(
					modifier = Modifier.fillMaxWidth(),
					text = "Войти",
					onClick = { intent.checkIsAuthorized() },
				)
				TransparentButton(
					modifier = Modifier
						.fillMaxWidth()
						.padding(
							top = Deps.Spacing.standardMargin,
							bottom = Deps.Spacing.standardMargin,
						),
					text = "Зарегистрироваться",
					onClick = {
						bottomSheetState.value = BottomSheetInfo(
							title = "Пароль не совпадает\nс предыдущим",
							buttons = listOf(
								ButtonView.Primary(
									modifier = Modifier.fillMaxWidth(),
									text = "Повторить попытку",
									color = ComposeColors.Green,
									onClick = { bottomSheetState.value = null }
								)
							)
						)
					},
				)
				Text(
					text = "Версия $appVersion",
					fontSize = Headings.H6.px.sp,
					color = ComposeColors.DescriptionGray,
				)
			}
		}
	}
}