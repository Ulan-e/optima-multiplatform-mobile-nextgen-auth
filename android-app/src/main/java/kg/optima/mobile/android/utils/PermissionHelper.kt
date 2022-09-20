package kg.optima.mobile.android.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

object PermissionsHelper {

    fun requestCameraPermission(
        activity: ComponentActivity,
        action: (isGranted: Boolean, msg: String?) -> Unit
    ) =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                when {
                    isGranted -> {
                        action(true, "Granted")
                        return@registerForActivityResult
                    }
                    !activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                        action(false, "shouldShowRequestPermissionRationale")
                        return@registerForActivityResult
                    }
                    else -> {
                        action(false, null)
                        return@registerForActivityResult
                    }
                }
            }
        }

    fun isGrantCameraPermission(
        context: Context,
        resultLauncher: ActivityResultLauncher<String>,
        action: (Boolean) -> Unit
    ) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            action(true)
        } else {
            resultLauncher.launch(Manifest.permission.CAMERA)
            action(false)
        }
    }
}