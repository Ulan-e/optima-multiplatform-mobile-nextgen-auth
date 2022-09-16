package kg.optima.mobile.android.ui.features.biometrics

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kg.optima.mobile.R
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.android.ui.features.registration.PasswordScreen
import kg.optima.mobile.android.ui.features.registration.self_confirm.SelfConfirmScreen
import kg.optima.mobile.android.utils.readTextFile
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.toolbars.NavigationIcon
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarInfo
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.registration.RegistrationFeatureFactory
import kg.optima.mobile.registration.data.component.RegistrationPreferences
import kg.optima.mobile.registration.presentation.liveness.LivenessIntent
import kg.optima.mobile.registration.presentation.liveness.LivenessState
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
import org.koin.androidx.compose.inject

class LivenessActivity : AppCompatActivity(), ICameraCaptureListener {

    private var cameraComponent: CameraCaptureComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

        }

        cameraComponent?.cameraCaptureListener = this
        LivenessInitializer.init()
    }

    override fun onStop() {
        cameraComponent!!.stopProcessing()
        cameraComponent!!.stopPreview()
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
        // biometricsRepository.putLivenessStatus(result.status)
        // btnContinue.visibility = View.VISIBLE
    }

    override fun onUpdateOverlay(direction: Direction, hint: Hint) {

    }

    override fun onBackPressed() {

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val serverUrl = "https://veritest.optima24.kg/vl/verilive"
    }
}

object LivenessScreen : Screen {

    @Composable
    override fun Content() {
        val product = remember {
            RegistrationFeatureFactory.create<LivenessIntent, LivenessState>()
        }

        val intent = product.intent
        val state = product.state

        val model by state.stateFlow.collectAsState(initial = State.StateModel.Initial)

        val registrationPreferences: RegistrationPreferences by inject()
        val navigator = LocalNavigator.currentOrThrow

        MainContainer(
            mainState = model,
            contentModifier = Modifier.fillMaxSize(),
            toolbarInfo = ToolbarInfo(
                navigationIcon = NavigationIcon(onBackClick = { intent.pop() })
            ),
            contentHorizontalAlignment = Alignment.Start,
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    val myView =
                        LayoutInflater.from(context)
                            .inflate(R.layout.activity_liveness, null, false)
                    val cameraComponent =
                        myView.findViewById<CameraCaptureComponent>(R.id.camera_capture_component)
                    cameraComponent?.setConfig(readTextFile(context.resources.openRawResource(R.raw.liveness_config_ru)))
                    cameraComponent?.setServerURL(LivenessActivity.serverUrl)
                    cameraComponent!!.startPreview()

                    val accessToken = registrationPreferences.accessToken
                    val personId = registrationPreferences.personId
                    val sessionId = cameraComponent.startProcessing(accessToken, personId)
                    myView
                },
                update = { view -> }
            )

            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Deps.Spacing.standardMargin),
                text = "Согласен",
                color = ComposeColors.Green,
                onClick = {
                    navigator.push(SelfConfirmScreen)
                }
            )
        }
    }
}