package kg.optima.mobile.android.ui.features.biometrics

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.google.accompanist.insets.ProvideWindowInsets
import kg.optima.mobile.R
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.design_system.android.theme.Theme
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.registration.RegistrationFeatureFactory
import kg.optima.mobile.registration.data.component.RegistrationPreferences
import kg.optima.mobile.registration.presentation.liveness.LivenessIntent
import kg.optima.mobile.registration.presentation.liveness.LivenessState
import kg.optima.mobile.resources.Headings
import kz.verigram.veridoc.sdk.VeridocInitializer
import kz.verigram.veridoc.sdk.dependency.ICameraCaptureListener
import kz.verigram.veridoc.sdk.model.DocumentType
import kz.verigram.veridoc.sdk.model.Language
import kz.verigram.veridoc.sdk.model.RecognitionMode
import kz.verigram.veridoc.sdk.ui.CameraCaptureComponent
import org.koin.androidx.compose.inject

class DocumentScanActivity : AppCompatActivity() {

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
                            Navigator(DocumentScanScreen)
                        },
                    )
                }
            }
        }
        VeridocInitializer.init()
    }

    override fun onBackPressed() {
        // TODO Показать диалог
    }

    object DocumentScanScreen : Screen {
        @Composable
        override fun Content() {
            val product = remember {
                RegistrationFeatureFactory.create<LivenessIntent, LivenessState>()
            }

            val intent = product.intent
            val state = product.state

            val model by state.stateFlow.collectAsState(initial = State.StateModel.Initial)

            val context = LocalContext.current

            val bottomSheetState = remember { mutableStateOf<BottomSheetInfo?>(null) }
            val buttonAndTextVisibleState = remember { mutableStateOf(false) }

            MainContainer(
                mainState = model,
                infoState = bottomSheetState.value,
                contentModifier = Modifier.fillMaxSize(),
                contentHorizontalAlignment = Alignment.Start,
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { context ->
                            val rootView = LayoutInflater.from(context)
                                .inflate(R.layout.activity_document_scan, null, false)
                            val cameraComponent = rootView
                                .findViewById<CameraCaptureComponent>(R.id.camera_capture_doc)

                            cameraComponent?.setLanguage(Language.RU)
                            cameraComponent?.setRecognitionMode(RecognitionMode.TWO_SIDED_DOCUMENT)
                            cameraComponent?.setDocumentType(DocumentType.KG_ID)
                            cameraComponent?.setIsGlareCheckNeeded(true)
                            cameraComponent.setCameraCaptureListener(object : ICameraCaptureListener {
                                override fun onErrorCallback(result: java.util.HashMap<String, String>) {
                                    Log.d("tag", result.size.toString())
                                }

                                override fun onLogEventCallback(result: java.util.HashMap<String, String>) {
                                    result.entries.forEach { (key, value) ->
                                        Log.d("tag", " key-> $key value-> $value ")
                                    }
                                }

                                override fun onSuccessCallback(result: java.util.HashMap<String, String>) {
                                    buttonAndTextVisibleState.value = true
                                }
                            })

                            cameraComponent?.start()
                            rootView
                        }
                    )

                    TopAppBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopStart),
                        title = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Сканирование паспорта",
                                    fontSize = Headings.H3.px.sp,
                                    maxLines = 1,
                                    textAlign = TextAlign.Center,
                                    color = ComposeColors.PrimaryWhite,
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                bottomSheetState.value = showBottomSheetDialog(
                                    positiveButton = {},
                                    negativeButton = { bottomSheetState.value = null }
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = null,
                                    tint = ComposeColors.PrimaryWhite,
                                )
                            }
                        },
                        actions = {
                            Spacer(Modifier.width(64.dp))
                        },
                        backgroundColor = ComposeColors.PrimaryBlack,
                        elevation = 0.dp,
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                    ) {
                        AnimatedVisibility(
                            visible = buttonAndTextVisibleState.value,
                            enter = fadeIn(animationSpec = tween(100))
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = Deps.Spacing.standardMargin,
                                        end = Deps.Spacing.standardMargin,
                                        bottom = 180.dp
                                    ),
                                text = "Готово!",
                                fontSize = Headings.H3.px.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                textAlign = TextAlign.Center,
                                color = ComposeColors.Green,
                            )

                            PrimaryButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = Deps.Spacing.standardMargin,
                                        end = Deps.Spacing.standardMargin,
                                        top = 94.dp
                                    ),
                                text = "Продолжить",
                                color = ComposeColors.Green,
                                onClick = {
                                    val livenessIntent =
                                        Intent(context, LivenessActivity::class.java)
                                    context.startActivity(livenessIntent)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onStop() {
        cameraComponent?.stop()
        super.onStop()
    }
}