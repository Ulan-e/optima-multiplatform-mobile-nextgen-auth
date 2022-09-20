package kg.optima.mobile.android.ui.features.biometrics

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
import kg.optima.mobile.android.utils.loadFile
import kg.optima.mobile.android.utils.readTextFile
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView
import kg.optima.mobile.design_system.android.utils.resources.ComposeColor
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.feature.registration.RegistrationScreenModel
import kg.optima.mobile.registration.RegistrationFeatureFactory
import kg.optima.mobile.registration.data.component.RegistrationPreferences
import kg.optima.mobile.registration.presentation.liveness.LivenessIntent
import kg.optima.mobile.registration.presentation.liveness.LivenessState
import kg.optima.mobile.resources.Headings
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

object LivenessScreen : Screen {

    private const val serverUrl = "https://veritest.optima24.kg/vl/verilive"

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
        val registrationPreferences: RegistrationPreferences by inject()

        val btnContinueVisibility = remember { mutableStateOf(false) }
        val btnContinueEnability = remember { mutableStateOf(true) }
        val livenessSessionId = remember { mutableStateOf("") }
        val livenessResult = remember { mutableStateOf("") }

        when (val livenessModel = model) {
            is LivenessState.LivenessModel.Passed -> {
                state.setStateModel(State.StateModel.Initial)

                bottomSheetState.value = BottomSheetInfo(
                    title = livenessModel.message,
                    buttons = listOf(
                        ButtonView.Primary(
                            text = "Продолжить",
                            composeColor = ComposeColor.composeColor(ComposeColors.Green),
                            onClickListener = ButtonView.OnClickListener.onClickListener {
                                context.navigateTo(RegistrationScreenModel.ControlQuestion)
                            }
                        )
                    )
                )
            }
            is LivenessState.LivenessModel.Failed -> {
                bottomSheetState.value = BottomSheetInfo(
                    title = "errorState.error",
                    buttons = listOf(
                        ButtonView.Primary(
                            text = "Повторить попытку",
                            composeColor = ComposeColor.composeColor(ComposeColors.PrimaryRed),
                            onClickListener = ButtonView.OnClickListener.onClickListener {
                                context.navigateTo(LivenessActivity())
                            }
                        )
                    )
                )
            }
        }

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
            contentModifier = Modifier.fillMaxSize(),
            toolbarInfo = null,
            sheetInfo = bottomSheetState.value,
            contentHorizontalAlignment = Alignment.Start,
        ) {

            BackHandler { onBack() }

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
                                text = "Сканирование лица",
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

                AndroidView(modifier = Modifier
                    .fillMaxWidth()
                    .weight(10f),
                    factory = { context ->
                        val rootView = LayoutInflater.from(context)
                            .inflate(R.layout.activity_liveness, null, false)
                        val cameraComponent =
                            rootView.findViewById<CameraCaptureComponent>(R.id.camera_capture_component)
                        cameraComponent?.setConfig(
                            readTextFile(
                                context.resources.openRawResource(R.raw.liveness_config_ru)
                            )
                        )
                        cameraComponent?.setServerURL(serverUrl)
                        cameraComponent!!.startPreview()

                        cameraComponent.cameraCaptureListener =
                            object : ICameraCaptureListener {
                                override fun onLivenessError(e: Throwable) {
                                    if (e is ConnectionException) {
                                        bottomSheetState.value = showBottomSheetDialog(
                                            title = "Отсутствует интернет \nсоединение",
                                            subTitle = "Проверьте наличие интернета \nна Вашем устройстве",
                                            positiveButtonView = ButtonView.Primary(
                                                text = "Повторить попытку",
                                                onClickListener = ButtonView.OnClickListener.onClickListener {
                                                    bottomSheetState.value = null
                                                }
                                            ),
                                        )
                                    }
                                    if (e is ServerResponseException || e is JsonFormatException) {
                                        bottomSheetState.value = showBottomSheetDialog(
                                            title = "Процесс идентификации \nостановлен",
                                            subTitle = "ServerResponseException ${e.localizedMessage}",
                                            positiveButtonView = ButtonView.Primary(
                                                text = "Понятно",
                                                onClickListener = ButtonView.OnClickListener.onClickListener {
                                                    bottomSheetState.value = null
                                                }
                                            ),
                                        )
                                    }
                                    if (e is CameraException) {
                                        bottomSheetState.value = showBottomSheetDialog(
                                            title = "Нельзя пройти \nподтверждение личности\n без доступа к камере",
                                            positiveButtonView = ButtonView.Primary(
                                                text = "Понятно",
                                                onClickListener = ButtonView.OnClickListener.onClickListener {
                                                    context.navigateTo(RegistrationScreenModel.SelfConfirm)
                                                }
                                            ),
                                        )
                                    }
                                }

                                override fun onLivenessFailed(result: LivenessResult) {
                                    bottomSheetState.value = showBottomSheetDialog(
                                        title = "Что-то пошло не так",
                                        subTitle = "Решите свой вопрос через нашу заботливую \nподдержку прямо сейчас",
                                        positiveButtonView = ButtonView.Primary(
                                            text = "Связаться с банком",
                                            onClickListener = ButtonView.OnClickListener.onClickListener {
                                                // TODO  retry
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

                                override fun onLivenessPassed(result: LivenessResult) {
                                    btnContinueVisibility.value = true
                                    result.status?.let { livenessResult.value = it }
                                }

                                override fun onUpdateOverlay(direction: Direction, hint: Hint) {

                                }
                            }

                        val accessToken = registrationPreferences.accessToken
                        val personId = registrationPreferences.personId
                        val sessionId = cameraComponent.startProcessing(accessToken, personId)
                        livenessSessionId.value = sessionId
                        rootView
                    })

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
                        PrimaryButton(modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = Deps.Spacing.standardMargin,
                                end = Deps.Spacing.standardMargin,
                                top = 44.dp
                            ),
                            enabled = btnContinueEnability.value,
                            text = "Продолжить",
                            color = ComposeColors.Green,
                            onClick = {
                                // context.navigateTo(RegistrationScreenModel.ControlQuestion)

                                // отправка данных для верификации клиента

                                val data = context.loadFile("scanned_file")
                                intent.verify(
                                    livenessResult = livenessResult.value,
                                    sessionId = livenessSessionId.value,
                                    data = data
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}