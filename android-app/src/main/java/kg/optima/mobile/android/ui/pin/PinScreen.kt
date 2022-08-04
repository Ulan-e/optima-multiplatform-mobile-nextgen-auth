package kg.optima.mobile.android.ui.pin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.ui.input.CodeInput
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.ComposeColors
import kg.optima.mobile.resources.Headings


object PinScreen : Screen {

	@Composable
	override fun Content() {
		val headerState = remember { mutableStateOf("Установить новый PIN-код") }
		val subheaderState = remember { mutableStateOf("для быстрого входа в приложение") }
		val codeState = remember { mutableStateOf(emptyString) }

		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(top = Deps.Spacing.marginFromTop),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Text(
				text = headerState.value,
				fontSize = Headings.H1.px.sp,
				fontWeight = FontWeight.Bold,
				color = ComposeColors.PrimaryDisabledGray,
			)
			Text(
				text = subheaderState.value,
				modifier = Modifier.padding(top = Deps.Spacing.subheaderMargin),
				fontSize = Headings.H5.px.sp,
				color = ComposeColors.DescriptionGray,
			)
			CodeInput(
				modifier = Modifier.padding(
					top = Deps.Spacing.marginFromHeader,
				),
				value = codeState.value,
				onValueChanged = { codeState.value = it }
			)
		}
	}
}