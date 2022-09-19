package kg.optima.mobile.android.ui.features.common

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kg.optima.mobile.android.ui.base.Router
import kg.optima.mobile.android.ui.features.welcome.WelcomeScreen
import kg.optima.mobile.android.utils.asActivity
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.base.presentation.permissions.Permission
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView
import kg.optima.mobile.design_system.android.ui.progressbars.CircularProgress
import kg.optima.mobile.design_system.android.ui.toolbars.MainToolbar
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarInfo
import kg.optima.mobile.design_system.android.utils.resources.ComposeColor
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.navigation.root.Root
import org.koin.androidx.compose.inject

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainContainer(
	modifier: Modifier = Modifier,
	mainState: State.StateModel?,
	infoState: BottomSheetInfo? = null,
	permissionController: PermissionController? = null,
	component: Root.Child.Component? = null,
	toolbarInfo: ToolbarInfo? = ToolbarInfo(),
	scrollable: Boolean = false,
    contentModifier: Modifier = Modifier.padding(all = Deps.Spacing.standardPadding),
    contentHorizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit,
) {
	val router: Router by inject()

	val context = LocalContext.current
	val navigator = LocalNavigator.currentOrThrow
	val bottomSheetNavigator = LocalBottomSheetNavigator.current

	if (!navigator.canPop) {
		val activity = LocalContext.current.asActivity()
		BackHandler {
			if (navigator.lastItem != WelcomeScreen) {
				navigator.replace(WelcomeScreen)
			} else {
				activity?.finish()
			}
		}
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
//                component?.addAll(mainState.screenModels)
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
									text = "Повторить попытку",
									composeColor = ComposeColor.composeColor(ComposeColors.Green),
									onClickListener = ButtonView.OnClickListener.onClickListener {
										bottomSheetNavigator.pop()
									}
								)
							)
						))
				}
			}
			is State.StateModel.RequestPermissions -> {
				val permissionsState = rememberMultiplePermissionsState(
					mainState.permissions.map {
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
								permissions = mainState.permissions
							)
						)
					}
				}
			}
			is State.StateModel.CustomPermissionRequired -> {
				bottomSheetNavigator.show(
					info = BottomSheetInfo(
						title = mainState.text,
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
								onClickListener = ButtonView.OnClickListener.onClickListener {
									bottomSheetNavigator.hide()
								}
							)
						)
					)
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

fun interface PermissionController {
	fun requestPermissionResult(state: State)

	sealed interface State {
		object Accepted : State
		class DeniedAlways(val permissions: List<Permission>) : State {
			constructor(permission: Permission) : this(listOf(permission))
		}
	}
}