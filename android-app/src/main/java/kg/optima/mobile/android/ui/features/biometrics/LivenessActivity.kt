package kg.optima.mobile.android.ui.features.biometrics

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kg.optima.mobile.R
import kg.optima.mobile.android.utils.readTextFile
import kg.optima.mobile.registration.data.component.RegistrationPreferences
import kz.verigram.verilive.sdk.LivenessInitializer
import kz.verigram.verilive.sdk.data.verification.entities.LivenessResult
import kz.verigram.verilive.sdk.domain.CameraException
import kz.verigram.verilive.sdk.domain.ConnectionException
import kz.verigram.verilive.sdk.domain.JsonFormatException
import kz.verigram.verilive.sdk.domain.ServerResponseException
import kz.verigram.verilive.sdk.domain.config.Hint
import kz.verigram.verilive.sdk.interfaces.ICameraCaptureListener
import kz.verigram.verilive.sdk.ui.CameraCaptureComponent
import kz.verigram.verilive.sdk.ui.entities.Direction
import org.koin.android.ext.android.inject

class LivenessActivity : AppCompatActivity(), ICameraCaptureListener {

    private var cameraCapture: CameraCaptureComponent? = null

    private val registrationPreferences: RegistrationPreferences by inject()

    private lateinit var btnContinue: Button
    private lateinit var btnBack: ImageButton
    private var sessionId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liveness)

        btnContinue = findViewById(R.id.btn_continue)
        btnBack = findViewById(R.id.btn_back)

        btnBack.setOnClickListener {
            // TODO Показать диалог
        }

        setLivenessConfigs()
        setBtnContinueClickListener()
        LivenessInitializer.init()
    }

    override fun onStart() {
        super.onStart()
        cameraCapture!!.startPreview()

        Log.d("terra", "registrationPreferences ${registrationPreferences.accessToken}")
        Log.d("terra", "registrationPreferences ${registrationPreferences.personId}")

        //  TODO Получение token & personId и сохранение sessionId
        val accessToken = registrationPreferences.accessToken
        val personId = registrationPreferences.personId

        sessionId = cameraCapture!!.startProcessing(accessToken, personId)
        // biometricsRepository.putSessionId(sessionId)
    }

    override fun onStop() {
        cameraCapture!!.stopProcessing()
        cameraCapture!!.stopPreview()
        super.onStop()
    }

    override fun onLivenessError(e: Throwable) {
        if (e is ConnectionException) {
            showToast("Network error. Please check you connection ${e.localizedMessage}")
        }
        if (e is ServerResponseException || e is JsonFormatException) {
            showToast("ServerResponseException ${e.localizedMessage}")
        }
        if (e is CameraException) {
            showToast("Error initializing camera")
        }
    }

    override fun onLivenessFailed(result: LivenessResult) {
        Log.d("onLivenessFailed", "onLivenessFailed")
    }

    override fun onLivenessPassed(result: LivenessResult) {
        doOnLivenessPassed(result = result)
        btnContinue.visibility = View.VISIBLE
    }

    override fun onUpdateOverlay(direction: Direction, hint: Hint) {

    }

    override fun onBackPressed() {

    }

    private fun doOnLivenessPassed(result: LivenessResult) {
        //  biometricsRepository.putLivenessStatus(result.status)
    }

    private fun setLivenessConfigs() {
        cameraCapture = findViewById(R.id.camera_capture_component)
        cameraCapture?.setServerURL(serverUrl)
        cameraCapture?.cameraCaptureListener = this
        cameraCapture?.setConfig(readTextFile(resources.openRawResource(R.raw.liveness_config_ru)))
    }

    private fun setBtnContinueClickListener() {
        btnContinue.setOnClickListener {
            // TODO Переход на следующий экран
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val serverUrl = "https://veritest.optima24.kg/vl/verilive"
    }
}