package kg.optima.mobile.android.ui.base.permission

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kg.optima.mobile.base.presentation.BaseMppState
import kg.optima.mobile.base.presentation.permissions.Permission
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView

var customRationaleRequest = CustomRationale.FIRST_REQUEST

@SuppressLint("ComposableNaming")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun requestPermission(
	requestPermissionState: BaseMppState.StateModel.RequestPermissions,
	permissionController: PermissionController?,
) {
	val permissionsState = rememberMultiplePermissionsState(
		requestPermissionState.permissions.map {
			when (it) {
				Permission.Camera -> Manifest.permission.CAMERA
				Permission.Storage -> Manifest.permission.READ_EXTERNAL_STORAGE
			}
		}
	)

	when {
		permissionsState.allPermissionsGranted -> {
			permissionController?.requestPermissionResult(PermissionController.State.Accepted)
		}
		else -> {
			if (permissionsState.shouldShowRationale && CustomRationale.THIRD_REQUEST == customRationaleRequest) {
				LaunchedEffect(key1 = Unit) {
					permissionsState.launchMultiplePermissionRequest()
					customRationaleRequest = CustomRationale.FOURTH_REQUEST
				}
			} else {
				if (CustomRationale.FIRST_REQUEST == customRationaleRequest) {
					LaunchedEffect(key1 = Unit) {
						permissionsState.launchMultiplePermissionRequest()
						customRationaleRequest = CustomRationale.SECOND_REQUEST
					}
				} else {
					customRationaleRequest =
						if (customRationaleRequest == CustomRationale.SECOND_REQUEST) CustomRationale.THIRD_REQUEST
						else CustomRationale.FOURTH_REQUEST
					permissionController?.requestPermissionResult(
						PermissionController.State.DeniedAlways(
							permissions = requestPermissionState.permissions
						)
					)
				}
			}
		}
	}
}

@SuppressLint("ComposableNaming")
@Composable
fun customPermissionRequired(
	customPermissionRequired: BaseMppState.StateModel.CustomPermissionRequired,
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
					onClickListener = ButtonView.onClickListener {
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
					onClickListener = ButtonView.onClickListener(onBottomSheetHidden)
				)
			)
		)
	)
}

enum class CustomRationale {
	FIRST_REQUEST, SECOND_REQUEST, THIRD_REQUEST, FOURTH_REQUEST,
}
