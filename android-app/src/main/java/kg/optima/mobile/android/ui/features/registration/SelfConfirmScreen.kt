package kg.optima.mobile.android.ui.features.registration

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.design_system.android.ui.animation.FadeInAnim
import kg.optima.mobile.design_system.android.ui.animation.FadeInAnimModel
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.text_fields.TitleTextField
import kg.optima.mobile.design_system.android.ui.toolbars.NavigationIcon
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarInfo
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.resId
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.images.RegistrationImages
import kotlinx.coroutines.delay


private val items = listOf(
	FadeInAnimModel(
		durationMillis = 0,
		delayMillis = 0,
		resId = RegistrationImages.passport.resId(),
		text = "Паспорта КР - ID карта (серии AN или ID)",
	),
	FadeInAnimModel(
		delayMillis = 1000,
		resId = RegistrationImages.sun.resId(),
		text = "Хорошее освещение",
	),
	FadeInAnimModel(
		delayMillis = 2000,
		resId = RegistrationImages.smile.resId(),
		text = "Нейтральное выражение лица",
	),
	FadeInAnimModel(
		delayMillis = 3000,
		resId = RegistrationImages.glasses.resId(),
		text = "Отсутствие очков или друких аксессуаров на лице",
	),
	FadeInAnimModel(
		delayMillis = 4000,
		resId = RegistrationImages.fullscreen.resId(),
		text = "Лицо по центру экрана, без движений",
	),
	FadeInAnimModel(
		delayMillis = 5000,
		resId = RegistrationImages.person.resId(),
		text = "Волосы собраны и не закрывают лицо, на камере не должно быть других людей",
	)
)

@Suppress("SameParameterValue")
object SelfConfirmScreen : Screen {

	@Composable
	override fun Content() {
		var buttonEnabled by remember { mutableStateOf(false) }

		LaunchedEffect(key1 = !buttonEnabled) {
			delay(6000)
			buttonEnabled = true
		}

		MainContainer(
			mainState = null,
			toolbarInfo = ToolbarInfo(
				navigationIcon = NavigationIcon(onBackClick = { })
			),
			contentHorizontalAlignment = Alignment.Start,
		) {
			TitleTextField(
				modifier = Modifier.padding(top = Deps.Spacing.bigMarginTop),
				text = "Подтверждение личности",
			)
			Text(
				modifier = Modifier.padding(top = Deps.Spacing.marginFromTitle),
				text = "Для прохождении онлайн идентификации необходимо:",
				fontSize = Headings.H3.sp,
			)
			IconTextFields(
				modifier = Modifier.padding(top = Deps.Spacing.standardMargin * 2),
				items = items,
			)
			Spacer(modifier = Modifier.weight(1f))
			PrimaryButton(
				modifier = Modifier.fillMaxWidth(),
				text = "Начать",
				enabled = buttonEnabled,
				color = ComposeColors.Green,
				onClick = { }
			)
		}
	}

	@Composable
	private fun IconTextFields(
		modifier: Modifier = Modifier,
		items: List<FadeInAnimModel>,
	) {
		LazyColumn(modifier = modifier.fillMaxWidth()) {
			items(items.size) {
				FadeInAnim(items[it])
			}
		}
	}
}