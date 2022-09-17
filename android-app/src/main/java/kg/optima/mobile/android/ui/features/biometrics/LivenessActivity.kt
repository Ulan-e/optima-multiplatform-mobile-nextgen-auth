package kg.optima.mobile.android.ui.features.biometrics

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.google.accompanist.insets.ProvideWindowInsets
import kg.optima.mobile.R
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.android.ui.startContent
import kg.optima.mobile.android.utils.loadFile
import kg.optima.mobile.android.utils.readTextFile
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.design_system.android.theme.Theme
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.toolbars.NavigationIcon
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarContent
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

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProvideWindowInsets {
                Theme.OptimaTheme {
                    BottomSheetNavigator(
                        sheetElevation = 0.dp,
                        sheetBackgroundColor = Color.Transparent,
                        sheetShape = RoundedCornerShape(16.dp, 16.dp),
                        content = {
                            Navigator(LivenessScreen)
                        },
                    )
                }
            }
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
            val context = LocalContext.current
            val bottomSheetState = remember { mutableStateOf<BottomSheetInfo?>(null) }

            MainContainer(
                mainState = model,
                contentModifier = Modifier.fillMaxSize(),
                infoState = bottomSheetState.value,
                contentHorizontalAlignment = Alignment.Start,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {

                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { context ->
                            val rootView = LayoutInflater
                                .from(context)
                                .inflate(R.layout.activity_liveness, null, false)
                            val cameraComponent = rootView
                                .findViewById<CameraCaptureComponent>(R.id.camera_capture_component)
                            cameraComponent?.setConfig(
                                readTextFile(
                                    context.resources.openRawResource(R.raw.liveness_config_ru)
                                )
                            )
                            cameraComponent?.setServerURL(serverUrl)
                            cameraComponent!!.startPreview()

                            val accessToken = registrationPreferences.accessToken
                            val personId = registrationPreferences.personId
                            val sessionId = cameraComponent.startProcessing(accessToken, personId)
                            rootView
                        }
                    )

                    TopAppBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .align(Alignment.TopStart)
                    ) {
                        IconButton(onClick = { }) {
                            Icon(Icons.Filled.Close, contentDescription = "Close")
                        }
                        Text("Сканирование лица", fontSize = 18.sp)
                    }

                    PrimaryButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(
                                horizontal = Deps.Spacing.standardMargin,
                                vertical = Deps.Spacing.standardMargin
                            ),
                        text = "Продолжить",
                        color = ComposeColors.Green,
                        onClick = {
                            val data = context.loadFile("scanned_file")
                            intent.verify(
                                livenessResult = "real",
                                sessionId = "sessionId",
                                data = data
                            )
                        }
                    )
                }
            }
        }
    }
}