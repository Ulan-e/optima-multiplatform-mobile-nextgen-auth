package kg.optima.mobile.android.ui.features.biometrics

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kg.optima.mobile.android.utils.PermissionsHelper
import kg.optima.mobile.databinding.ActivityStartBiometricsBinding
import kz.verigram.veridoc.sdk.VeridocInitializer
import kz.verigram.verilive.sdk.LivenessInitializer

class StartBiometricsActivity : AppCompatActivity() {

    private val tag = StartBiometricsActivity::class.java.simpleName
    private lateinit var binding: ActivityStartBiometricsBinding

    private val permissionHelper =
        PermissionsHelper.requestCameraPermission(this) { isGranted, message ->
            binding.btnStart.isEnabled = isGranted
            Log.d(tag, message!!)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBiometricsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkCameraPermission()
        navigateToLivenessScreen()
    }

    private fun checkCameraPermission() {
        PermissionsHelper.isGrantCameraPermission(this, permissionHelper) { isGranted ->
            binding.btnStart.isEnabled = isGranted
        }
    }

    private fun navigateToLivenessScreen() {
        binding.btnStart.setOnClickListener {
            VeridocInitializer.init()
            startActivity(Intent(this, DocumentScanActivity::class.java))
        }
    }

    override fun onBackPressed() {
        finish()
    }
}