package kg.optima.mobile.android.ui.features.common

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kg.optima.mobile.android.ui.base.Router
import kg.optima.mobile.android.utils.asActivity
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView
import kg.optima.mobile.design_system.android.ui.progressbars.CircularProgress
import kg.optima.mobile.design_system.android.ui.toolbars.MainToolbar
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarInfo
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.navigation.root.Root
import org.koin.androidx.compose.inject

@Composable
fun MainContainer(
    modifier: Modifier = Modifier,
    mainState: State.StateModel?,
    infoState: BottomSheetInfo? = null,
    component: Root.Child.Component? = null,
    toolbarInfo: ToolbarInfo? = null,
    contentModifier: Modifier = Modifier
        .fillMaxSize()
        .padding(all = Deps.Spacing.standardPadding)
        .background(ComposeColors.Background),
    contentHorizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit,
) {
    val router: Router by inject()

    val navigator = LocalNavigator.currentOrThrow
    val bottomSheetNavigator = LocalBottomSheetNavigator.current

    if (!navigator.canPop) {
        val activity = LocalContext.current.asActivity()
        BackHandler { activity?.finish() }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (infoState != null) {
            bottomSheetNavigator.show(infoState)
        } else {
            bottomSheetNavigator.hide()

        }
        when (mainState) {
            is State.StateModel.Loading -> {
                CircularProgress(modifier = Modifier.align(Alignment.Center))
            }
            is State.StateModel.Navigate -> {
                component?.addAll(mainState.screenModels)
                router.push(mainState.screenModels)
            }
            is State.StateModel.Pop -> {
                navigator.pop()
            }
            is State.StateModel.Error -> {
                // TODO process error
                when (val errorState = mainState) {
                    is State.StateModel.Error.BaseError -> bottomSheetNavigator.show(
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



        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ComposeColors.Background)
        ) {
            toolbarInfo?.let { MainToolbar(it) }
            Column(
                modifier = contentModifier,
                horizontalAlignment = contentHorizontalAlignment,
                content = content,
            )
        }
    }
}

