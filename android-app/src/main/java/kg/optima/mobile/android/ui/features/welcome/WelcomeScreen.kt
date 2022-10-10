package kg.optima.mobile.android.ui.features.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.base.MainContainer
import kg.optima.mobile.android.ui.features.auth.login.LoginScreen
import kg.optima.mobile.android.utils.Constants
import kg.optima.mobile.android.utils.appVersion
import kg.optima.mobile.android.utils.asBaseActivity
import kg.optima.mobile.base.di.create
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.common.CommonFeatureFactory
import kg.optima.mobile.common.presentation.welcome.WelcomeIntent
import kg.optima.mobile.common.presentation.welcome.WelcomeState
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.buttons.TransparentButton
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarInfo
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.Headings

@Parcelize
object WelcomeScreen : BaseScreen {

	@OptIn(ExperimentalMaterialApi::class)
	@Composable
	override fun Content() {
		val product = remember {
			CommonFeatureFactory.create<WelcomeIntent, WelcomeState>()
		}
		val state = product.state
		val intent = product.intent

		val activity = LocalContext.current.asBaseActivity()

		val onBackClicked =
			activity?.navigationController?.get(Constants.LOGIN_SCREEN_ON_BACK_CLICKED)

		val model by state.stateFlow.collectAsState(
			initial = if (onBackClicked == true) null else UiState.Model.Initial
		)

		val bottomSheetState = remember { mutableStateOf<BottomSheetInfo?>(null) }

		when (model) {
			UiState.Model.Initial -> intent.init()
		}

		MainContainer(
			mainState = model,
			sheetInfo = bottomSheetState.value,
			toolbarInfo = ToolbarInfo(navigationIcon = null),
		) {
			Column(
				modifier = Modifier.weight(4f),
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center,
			) {
				Text(
					text = "Добро пожаловать!",
					fontSize = Headings.H1.sp,
					fontWeight = FontWeight.Bold,
				)
				Text(
					modifier = Modifier
						.padding(top = Deps.Spacing.standardMargin * 2),
					text = "Весь банк в одном приложении",
					fontSize = Headings.H4.sp,
				)
			}
			WelcomeScreenButtonBlock(
				modifier = Modifier.weight(5.5f),
				verticalAlignment = Alignment.Top,
				onMapClick = { intent.openMap() },
				onLangClick = { intent.openLanguages() },
				onContactsClick = { intent.openContacts() },
				onRatesClick = { intent.openRates() }
			)
			PrimaryButton(
				modifier = Modifier.fillMaxWidth(),
				text = "Войти",
				onClick = {
					activity?.navigationController?.set(Constants.LOGIN_SCREEN_ON_BACK_CLICKED, false)
					intent.login()
				},
			)
			TransparentButton(
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						top = Deps.Spacing.standardMargin,
						bottom = Deps.Spacing.standardMargin,
					),
				text = "Зарегистрироваться",
				onClick = intent::register,
			)
			Text(
				text = "Версия $appVersion",
				fontSize = Headings.H6.sp,
				color = ComposeColors.DescriptionGray,
			)
		}
	}
}