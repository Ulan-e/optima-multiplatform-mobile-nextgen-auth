package kg.optima.mobile.android.ui.features.biometrics

import android.util.Log
import android.view.LayoutInflater
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.R
import kg.optima.mobile.android.ui.features.biometrics.NavigationManager.navigateTo
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.android.utils.saveFile
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.feature.registration.RegistrationScreenModel
import kg.optima.mobile.registration.RegistrationFeatureFactory
import kg.optima.mobile.registration.presentation.liveness.LivenessIntent
import kg.optima.mobile.registration.presentation.liveness.LivenessState
import kg.optima.mobile.resources.Headings
import kz.verigram.veridoc.sdk.dependency.ICameraCaptureListener
import kz.verigram.veridoc.sdk.model.DocumentType
import kz.verigram.veridoc.sdk.model.Language
import kz.verigram.veridoc.sdk.model.RecognitionMode
import kz.verigram.veridoc.sdk.ui.CameraCaptureComponent

object DocumentScanScreen : Screen {

    @OptIn(ExperimentalMaterialApi::class)
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
        val btnContinueVisibility = remember { mutableStateOf(false) }

        val onBack = {
            bottomSheetState.value = showBottomSheetDialog(
                title = "Вы действительно хотите \nостановить идентификацию?",
                subTitle = "Идентификация не закончена. \nПроцесс будет остановлен и вы окажетесь \nна начальном экране",
                positiveButtonView = ButtonView.Primary(
                    text = "Остановить процесс",
                    onClickListener = ButtonView.OnClickListener.onClickListener {
                        context.navigateTo(RegistrationScreenModel.SelfConfirm)
                    }
                ),
                negativeButtonView = ButtonView.Transparent(
                    text = "Отмена",
                    onClickListener = ButtonView.OnClickListener.onClickListener {
                        bottomSheetState.value = null
                    }
                )
            )
        }

        MainContainer(
            mainState = model,
            sheetInfo = bottomSheetState.value,
            toolbarInfo = null,
            contentModifier = Modifier.fillMaxSize(),
            contentHorizontalAlignment = Alignment.Start,
        ) {

            BackHandler {
                onBack()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .weight(1f)
            ) {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
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
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = null,
                                tint = ComposeColors.PrimaryWhite,
                            )
                        }
                    },
                    actions = { Spacer(Modifier.width(64.dp)) },
                    backgroundColor = ComposeColors.PrimaryBlack,
                    elevation = 0.dp,
                )

                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(10f),
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
                            override fun onErrorCallback(result: HashMap<String, String>) {
                                result.entries.forEach { (key, value) ->
                                    Log.d("veridoc", "k $key v $value ")
                                }
                            }

                            override fun onLogEventCallback(result: HashMap<String, String>) {
                                result.entries.forEach { (key, value) ->
                                    Log.d("veridoc", "k $key v $value ")
                                }
                            }

                            override fun onSuccessCallback(result: HashMap<String, String>) {
                                context.saveFile(filename = "scanned_file", data = result)
                                btnContinueVisibility.value = true
                            }
                        })
                        cameraComponent?.start()
                        rootView
                    }
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                        .background(ComposeColors.PrimaryBlack),
                ) {
                    AnimatedVisibility(
                        visible = btnContinueVisibility.value,
                        enter = fadeIn(animationSpec = tween(100))
                    ) {
                        PrimaryButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = Deps.Spacing.standardMargin,
                                    end = Deps.Spacing.standardMargin,
                                    top = 44.dp
                                ),
                            text = "Продолжить",
                            color = ComposeColors.Green,
                            onClick = {
                                context.navigateTo(LivenessActivity())
                            }
                        )
                    }
                }
            }
        }
    }
}