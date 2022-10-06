package kg.optima.mobile.android.ui.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kg.optima.mobile.android.ui.base.permission.PermissionController
import kg.optima.mobile.android.ui.base.permission.customPermissionRequired
import kg.optima.mobile.android.ui.base.permission.requestPermission
import kg.optima.mobile.android.ui.base.processing.processError
import kg.optima.mobile.android.ui.base.routing.Router
import kg.optima.mobile.android.utils.asActivity
import kg.optima.mobile.base.presentation.BaseMppState
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.bottomsheet.InfoBottomSheet
import kg.optima.mobile.design_system.android.ui.progressbars.CircularProgress
import kg.optima.mobile.design_system.android.ui.toolbars.MainToolbar
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarInfo
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.navigation.root.Root
import kotlinx.coroutines.launch
import org.koin.androidx.compose.inject

typealias PopLast = () -> Unit

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainContainer(
	modifier: Modifier = Modifier,
	mainState: BaseMppState.StateModel?,
	sheetInfo: BottomSheetInfo? = null,
	permissionController: PermissionController? = null,
	component: Root.Child.Component? = null,
	toolbarInfo: ToolbarInfo? = ToolbarInfo(),
	scrollable: Boolean = false,
	contentModifier: Modifier = Modifier.padding(all = Deps.Spacing.standardPadding),
	contentHorizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
	onSheetStateChanged: (ModalBottomSheetValue, PopLast) -> Unit = { _, _ -> },
	sheetStatus: SheetStatus = SheetStatus.NOT_DISMISSIBLE,
	sheetNavigationScreen: Screen? = null,
	content: @Composable ColumnScope.(PopLast) -> Unit,
) {
	val router: Router by inject()

	val navigator = LocalNavigator.currentOrThrow
	val context = LocalContext.current
	val activity = context.asActivity()

	val focusManager = LocalFocusManager.current
	if (sheetInfo != null) {
		focusManager.clearFocus()
	}

	val coroutineScope = rememberCoroutineScope()
	val sheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		confirmStateChange = { sheetValue ->
			when (sheetStatus) {
				SheetStatus.DISMISS_POP -> {
					onSheetStateChanged(sheetValue) { navigator.pop() }
					sheetValue != ModalBottomSheetValue.HalfExpanded
				}
				SheetStatus.NOT_DISMISSIBLE -> {
					onSheetStateChanged(sheetValue) { }
					sheetValue != ModalBottomSheetValue.Hidden
				}
				SheetStatus.DISMISS -> {
					onSheetStateChanged(sheetValue) { }
					sheetValue != ModalBottomSheetValue.Expanded
				}
				SheetStatus.DISMISS_PUSH -> {
					onSheetStateChanged(sheetValue) {
						if (sheetNavigationScreen != null)
							navigator.push(sheetNavigationScreen)
					}
					sheetValue != ModalBottomSheetValue.Expanded
				}
			}
		}
	)
	val sheetInfoState = remember { mutableStateOf(sheetInfo) }
	val isLoading = remember { mutableStateOf(false) }

	val onChangeSheetState: (BottomSheetInfo?) -> Unit = {
		coroutineScope.launch {
			if (it != null) sheetState.show() else sheetState.hide()
			sheetInfoState.value = it
		}
	}
	val onBottomSheetHidden: () -> Unit = {
		coroutineScope.launch { sheetState.hide() }
	}

//	BackHandler(!navigator.canPop) {
//		if (navigator.lastItem != WelcomeScreen) {
//			navigator.replace(WelcomeScreen)
//		} else {
//			activity?.finish()
//		}
//	}

	onChangeSheetState(sheetInfo)

	ModalBottomSheetLayout(
		sheetBackgroundColor = Color.Transparent,
		sheetElevation = 0.dp,
		sheetContent = { InfoBottomSheet(bottomSheetInfo = sheetInfoState.value) },
		sheetShape = RoundedCornerShape(Deps.cornerRadius),
		sheetState = sheetState,
	) {
		Box(contentAlignment = Alignment.Center) {
			when (mainState) {
				is BaseMppState.StateModel.Navigate -> {
//                        component?.addAll(mainState.screenModels)
					router.push(mainState.screenModels)
				}
				is BaseMppState.StateModel.Pop -> {
//						component?.pop()
//						router.popLast()
				}
				is BaseMppState.StateModel.Error -> {
					processError(
						errorState = mainState,
						onSheetStateChanged = onChangeSheetState,
						onBottomSheetHidden = onBottomSheetHidden,
					)
				}
				is BaseMppState.StateModel.RequestPermissions -> {
					requestPermission(
						requestPermissionState = mainState,
						permissionController = permissionController,
					)
				}
				is BaseMppState.StateModel.CustomPermissionRequired -> {
					customPermissionRequired(
						customPermissionRequired = mainState,
						context = context,
						onSheetStateChanged = onChangeSheetState,
						onBottomSheetHidden = onBottomSheetHidden,
					)
				}
			}

			if (mainState is BaseMppState.StateModel.Loading) {
				CircularProgress(modifier = Modifier.align(Alignment.Center))
			}

			Column(
				modifier = Modifier
					.fillMaxSize()
					.background(ComposeColors.Background)
			) {
				if (toolbarInfo != null) {
					MainToolbar(
						toolbarInfo.copy(
							navigationIcon = toolbarInfo.navigationIcon?.copy(onBackClick = {
								navigator.pop()
							})
						)
					)
				}
				val columnModifier = contentModifier
					.fillMaxSize()
					.weight(1f, false)
					.background(ComposeColors.Background)
				if (scrollable)
					columnModifier.verticalScroll(rememberScrollState())

				Column(
					modifier = columnModifier,
					horizontalAlignment = contentHorizontalAlignment,
					content = { content { navigator.pop() } },
				)
			}
		}
	}

}
