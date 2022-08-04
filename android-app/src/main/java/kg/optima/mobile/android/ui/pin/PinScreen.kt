package kg.optima.mobile.android.ui.pin

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kg.optima.mobile.android.ui.main.MainScreen
import kg.optima.mobile.auth.presentation.pin_set.PinSetFactory
import kg.optima.mobile.auth.presentation.pin_set.PinSetIntentHandler
import kg.optima.mobile.auth.presentation.pin_set.PinSetStateMachine
import kg.optima.mobile.base.presentation.StateMachine
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.ui.input.CodeInput
import kg.optima.mobile.design_system.android.ui.pad.CellType
import kg.optima.mobile.design_system.android.ui.pad.NumberPad
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.ComposeColors
import kg.optima.mobile.resources.Headings


object PinScreen : Screen {

	@Composable
	override fun Content() {
		val stateMachine = PinSetFactory.stateMachine
		val intentHandler = PinSetFactory.intentHandler

		val navigator = LocalNavigator.currentOrThrow

		val state by stateMachine.state.collectAsState(initial = null)

		val headerState = remember { mutableStateOf("Установить новый PIN-код") }
		val subheaderState = remember { mutableStateOf("для быстрого входа в приложение") }
		val codeState = remember { mutableStateOf(emptyString) }

		when (val pinSetState = state) {
			is PinSetStateMachine.PinSetState -> {
				when (pinSetState) {
					PinSetStateMachine.PinSetState.Save -> {
						headerState.value = "Повторить PIN-код"
					}
					is PinSetStateMachine.PinSetState.Compare -> {
						if (pinSetState.isMatches) {
							navigator.replaceAll(MainScreen)
						} else {

						}
					}
				}
			}
			is StateMachine.State.Loading ->
				Log.d("MainScreen", "Loading State")
			is StateMachine.State.Error ->
				Log.d("MainScreen", "Error State")
		}

		Column(
			modifier = Modifier
				.fillMaxSize(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center,
		) {
			Spacer(modifier = Modifier.weight(1f))
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
			Spacer(modifier = Modifier.weight(1f))
			CodeInput(
				value = codeState.value,
				onValueChanged = { codeState.value = it },
				onInputCompleted = {
					when (state) {
						null -> {
							intentHandler.dispatch(PinSetIntentHandler.PinSetIntent.Save(codeState.value))
							codeState.value = emptyString
						}
						PinSetStateMachine.PinSetState.Save -> {
							intentHandler.dispatch(PinSetIntentHandler.PinSetIntent.Compare(codeState.value))
						}
					}
				},
			)
			Spacer(modifier = Modifier.weight(1f))
			NumberPad(
				modifier = Modifier.padding(
					start = Deps.Spacing.numPadXMargin,
					end = Deps.Spacing.numPadXMargin,
				),
				onClick = { type ->
					when (type) {
						is CellType.Img -> {
							if (codeState.value.isNotBlank()) {
								val sliced =
									codeState.value.slice(0 until codeState.value.length - 1)
								codeState.value = sliced
							}
						}
						is CellType.Num -> {
							codeState.value = codeState.value + type.num
						}
						is CellType.Text -> Unit
					}
				}
			)
			Spacer(modifier = Modifier.weight(1f))
		}
	}
}