package kg.optima.mobile.android.ui.features.common

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kg.optima.mobile.android.ui.base.Router
import kg.optima.mobile.android.ui.base.permission.PermissionController
import kg.optima.mobile.android.ui.features.welcome.WelcomeScreen
import kg.optima.mobile.android.utils.asActivity
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.base.presentation.permissions.Permission
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.bottomsheet.InfoBottomSheet
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView
import kg.optima.mobile.design_system.android.ui.progressbars.CircularProgress
import kg.optima.mobile.design_system.android.ui.toolbars.MainToolbar
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarInfo
import kg.optima.mobile.design_system.android.utils.resources.ComposeColor
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.navigation.root.Root
import kotlinx.coroutines.launch
import org.koin.androidx.compose.inject

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainContainer(
	modifier: Modifier = Modifier,
	mainState: State.StateModel?,
	sheetInfo: BottomSheetInfo? = null,
	permissionController: PermissionController? = null,
	component: Root.Child.Component? = null,
	toolbarInfo: ToolbarInfo? = ToolbarInfo(),
	scrollable: Boolean = false,
	contentModifier: Modifier = Modifier.padding(all = Deps.Spacing.standardPadding),
	contentHorizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
	onSheetStateChanged: (ModalBottomSheetValue) -> Unit = {},
	content: @Composable ColumnScope.() -> Unit,
) {
	val router: Router by inject()

	val context = LocalContext.current
	val activity = context.asActivity()
	val navigator = LocalNavigator.currentOrThrow

	val coroutineScope = rememberCoroutineScope()
	val sheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		confirmStateChange = { onSheetStateChanged(it); it != ModalBottomSheetValue.HalfExpanded }
	)
	val sheetInfoState = remember { mutableStateOf(sheetInfo) }

	val onSheetStateChanged: (BottomSheetInfo?) -> Unit = {
		coroutineScope.launch {
			if (it != null) sheetState.show() else sheetState.hide()
			sheetInfoState.value = it
		}
	}
	val onBottomSheetHidden: () -> Unit = {
		coroutineScope.launch { sheetState.hide() }
	}

	BackHandler(!navigator.canPop) {
		if (navigator.lastItem != WelcomeScreen) {
			navigator.replace(WelcomeScreen)
		} else {
			activity?.finish()
		}
	}

	onSheetStateChanged(sheetInfo)

	ModalBottomSheetLayout(
		sheetBackgroundColor = Color.Transparent,
		sheetElevation = 0.dp,
		sheetContent = { InfoBottomSheet(bottomSheetInfo = sheetInfoState.value) },
		sheetShape = RoundedCornerShape(Deps.cornerRadius),
		sheetState = sheetState,
	) {
		Box(modifier = modifier.fillMaxSize()) {
			when (mainState) {
				is State.StateModel.Loading -> {
					CircularProgress(modifier = Modifier.align(Alignment.Center))
				}
				is State.StateModel.Navigate -> {
//                component?.addAll(mainState.screenModels)
					router.push(mainState.screenModels)
				}
				is State.StateModel.Pop -> {
					navigator.pop()
				}
				is State.StateModel.Error -> {
					processError(
						errorState = mainState,
						onSheetStateChanged = onSheetStateChanged,
						onBottomSheetHidden = onBottomSheetHidden,
					)
				}
				is State.StateModel.RequestPermissions -> {
					requestPermission(
						requestPermissionState = mainState,
						permissionController = permissionController,
					)
				}
				is State.StateModel.CustomPermissionRequired -> {
					customPermissionRequired(
						customPermissionRequired = mainState,
						context = context,
						onSheetStateChanged = onSheetStateChanged,
						onBottomSheetHidden = onBottomSheetHidden,
					)
				}
			}

			Column(
				modifier = Modifier
					.fillMaxSize()
					.background(ComposeColors.Background)
			) {
				if (toolbarInfo != null) MainToolbar(toolbarInfo)
				val columnModifier = contentModifier
					.fillMaxSize()
					.weight(1f, false)
					.background(ComposeColors.Background)
				if (scrollable)
					columnModifier.verticalScroll(rememberScrollState())

				Column(
					modifier = columnModifier,
					horizontalAlignment = contentHorizontalAlignment,
					content = content,
				)
			}
		}
	}

}

@Composable
private fun processError(
	errorState: State.StateModel.Error,
	onSheetStateChanged: (sheetInfo: BottomSheetInfo?) -> Unit,
	onBottomSheetHidden: () -> Unit,
) {
	// TODO process error
	when (errorState) {
		is State.StateModel.Error.BaseError -> onSheetStateChanged(
			BottomSheetInfo(
				title = errorState.error,
				buttons = listOf(
					ButtonView.Primary(
						text = "Повторить попытку",
						composeColor = ComposeColor.composeColor(ComposeColors.Green),
						onClickListener = ButtonView.OnClickListener.onClickListener(
							onBottomSheetHidden
						)
					)
				)
			)
		)
	}
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun requestPermission(
	requestPermissionState: State.StateModel.RequestPermissions,
	permissionController: PermissionController?
) {
	val permissionsState = rememberMultiplePermissionsState(
		requestPermissionState.permissions.map {
			when (it) {
				Permission.Camera -> Manifest.permission.CAMERA
				Permission.Storage -> Manifest.permission.READ_EXTERNAL_STORAGE
			}
		}
	)
	if (permissionsState.allPermissionsGranted) {
		permissionController?.requestPermissionResult(PermissionController.State.Accepted)
	} else {
		if (permissionsState.shouldShowRationale) {
			var canRequested = true
			LaunchedEffect(key1 = canRequested) {
				canRequested = false
				permissionsState.launchMultiplePermissionRequest()
			}
		} else {
			permissionController?.requestPermissionResult(
				PermissionController.State.DeniedAlways(
					permissions = requestPermissionState.permissions
				)
			)
		}
	}

}

@Composable
private fun customPermissionRequired(
	customPermissionRequired: State.StateModel.CustomPermissionRequired,
	context: Context,
	onSheetStateChanged: (sheetInfo: BottomSheetInfo?) -> Unit,
	onBottomSheetHidden: () -> Unit,
) {
	onSheetStateChanged(
		BottomSheetInfo(
			title = customPermissionRequired.text,
			buttons = listOf(
				ButtonView.Primary(
					text = "Настройки",
					onClickListener = ButtonView.OnClickListener.onClickListener {
						val intent =
							Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
								.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
						val uri = Uri.fromParts("package", context.packageName, null)
						intent.data = uri
						context.startActivity(intent)
					}
				),
				ButtonView.Transparent(
					text = "Отмена",
					onClickListener = ButtonView.OnClickListener.onClickListener(onBottomSheetHidden)
				)
			)
		)
	)
}
