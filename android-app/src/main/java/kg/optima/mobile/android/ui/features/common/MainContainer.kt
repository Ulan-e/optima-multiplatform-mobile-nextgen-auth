package kg.optima.mobile.android.ui.features.common

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kg.optima.mobile.android.ui.base.Router
import kg.optima.mobile.android.utils.asActivity
import kg.optima.mobile.base.presentation.StateMachine
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView
import kg.optima.mobile.design_system.android.ui.progressbars.CircularProgress
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.navigation.root.Root
import org.koin.androidx.compose.inject

@Composable
fun MainContainer(
	modifier: Modifier = Modifier,
	mainState: StateMachine.State?,
	infoState: BottomSheetInfo? = null,
	component: Root.Child.Component? = null,
	content: @Composable () -> Unit,
) {
	val router: Router by inject()

	val navigator = LocalNavigator.currentOrThrow
	val bottomSheetNavigator = LocalBottomSheetNavigator.current

	if (!navigator.canPop) {
		val activity = LocalContext.current.asActivity()
		BackHandler { activity?.finish() }
	}

	Box(modifier = modifier.fillMaxSize()) {
		when (mainState) {
			is StateMachine.State.Loading -> {
				CircularProgress(modifier = Modifier.align(Alignment.Center))
			}
			is StateMachine.State.Navigate -> {
				component?.addAll(mainState.screenModels)
				router.push(mainState.screenModels)
			}
			is StateMachine.State.Pop -> {
				navigator.pop()
			}
			is StateMachine.State.Error -> {
				// TODO process error
				when (val errorState = mainState) {
					is StateMachine.State.Error.BaseError -> bottomSheetNavigator.show(
						info = BottomSheetInfo(
							title = errorState.error,
							buttons = listOf(
								ButtonView.Primary(
									modifier = Modifier.fillMaxWidth(),
									text = "Повторить попытку",
									color = ComposeColors.Green,
									onClick = { bottomSheetNavigator.pop() }
								)
							)
						))
				}
			}
		}

		if (infoState != null) {
			bottomSheetNavigator.show(infoState)
		} else {
			bottomSheetNavigator.hide()
		}

		content()
	}
}

