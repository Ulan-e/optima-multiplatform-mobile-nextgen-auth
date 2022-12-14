package kg.optima.mobile.android.ui.features.biometrics.liveness

import android.annotation.SuppressLint
import android.view.LayoutInflater
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.R
import kg.optima.mobile.android.ui.base.MainContainer
import kg.optima.mobile.android.utils.navigateTo
import kg.optima.mobile.android.utils.loadFile
import kg.optima.mobile.android.utils.readTextFile
import kg.optima.mobile.base.di.create
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.core.common.Constants
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView
import kg.optima.mobile.design_system.android.utils.resources.ComposeColor
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.values.Deps
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

class LivenessScreen : Screen {

	companion object {
		private const val serverUrl = "https://veritest.optima24.kg/vl/verilive"
	}

	@SuppressLint("InflateParams")
	@OptIn(ExperimentalMaterialApi::class)
	@Composable
	override fun Content() {
		val product = remember {
			RegistrationFeatureFactory.create<LivenessIntent, LivenessState>()
		}

		val intent = product.intent
		val state = product.state

		val model by state.stateFlow.collectAsState(initial = UiState.Model.Initial)

		val context = LocalContext.current

		val bottomSheetState = remember { mutableStateOf<BottomSheetInfo?>(null) }
		val registrationPreferences: RegistrationPreferences by inject()

		val btnContinueVisibility = remember { mutableStateOf(false) }
		val btnContinueEnability = remember { mutableStateOf(true) }
		val livenessSessionId = remember { mutableStateOf("") }
		val livenessResult = remember { mutableStateOf("") }

		when (val livenessModel: UiState.Model? = model) {
			is UiState.Model.Error.BaseError -> {
				intent.init()
				bottomSheetState.value = BottomSheetInfo(
					title = "???????????? ???????????????? ????????????????????. ????????????????????, ???????????????????? ?????????? ?????? ???????????????????? ?? ????????.",
					buttons = listOf(
						ButtonView.Primary(
							text = "?????????????????? ?? ????????????",
							composeColor = ComposeColor.composeColor(ComposeColors.PrimaryRed),
							onClickListener = ButtonView.onClickListener(intent::navigateToContacts)
						),
						ButtonView.Transparent(
							text = "????????????",
							onClickListener = ButtonView.onClickListener(intent::navigateToWelcome)
						)
					)
				)
			}
			is LivenessState.Model.Passed -> {
				intent.init()
				bottomSheetState.value = BottomSheetInfo(
					title = livenessModel.message.orEmpty(),
					buttons = listOf(
						ButtonView.Primary(
							text = "????????????????????",
							composeColor = ComposeColor.composeColor(ComposeColors.Green),
							onClickListener =
							ButtonView.onClickListener(intent::navigateToControlQuestion)
						)
					)
				)
			}
		}

		val onBack = {
			bottomSheetState.value = BottomSheetInfo(
				title = "???? ?????????????????????????? ???????????? \n???????????????????? ???????????????????????????",
				description = "?????????????????????????? ???? ??????????????????. \n?????????????? ?????????? ???????????????????? ?? ???? ?????????????????? \n???? ?????????????????? ????????????",
				buttons = listOf(
					ButtonView.Primary(
						text = "???????????????????? ??????????????",
						composeColor = ComposeColor.composeColor(ComposeColors.PrimaryRed),
						onClickListener = ButtonView.onClickListener(intent::navigateToSelfConfirm)
					),
					ButtonView.Transparent(
						text = "????????????",
						onClickListener = ButtonView.onClickListener {
							bottomSheetState.value = null
						}
					)
				)
			)
		}

		MainContainer(
			mainState = model,
			contentModifier = Modifier.fillMaxSize(),
			toolbarInfo = null,
			sheetInfo = bottomSheetState.value,
			onBackParameters = false to onBack,
			contentHorizontalAlignment = Alignment.Start
		) {
			Column {
				TopAppBar(
					modifier = Modifier.fillMaxWidth(),
					title = {
						Row(
							modifier = Modifier.fillMaxWidth(),
							horizontalArrangement = Arrangement.Center
						) {
							Text(
								modifier = Modifier.fillMaxWidth(),
								text = "???????????????????????? ????????",
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
						cameraComponent.setConfig(
							readTextFile(context.resources.openRawResource(R.raw.liveness_config_ru))
						)
						cameraComponent.setServerURL(serverUrl)
						cameraComponent.startPreview()

						cameraComponent.cameraCaptureListener = ICameraCaptureListenerImpl(
							onSheetValueChanged = { bottomSheetState.value = it },
							navigateToLivenessActivity = { context.navigateTo(LivenessActivity()) },
							navigateToSelfConfirm = intent::navigateToSelfConfirm,
							onLivenessResultPassed = { result ->
								btnContinueVisibility.value = true
								result.status?.let { livenessResult.value = it }
							}
						)

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
							text = "????????????????????",
							color = ComposeColors.Green,
							onClick = {
								val data = context.loadFile(Constants.DOCUMENT_FILE_NAME)
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

	inner class ICameraCaptureListenerImpl(
		private val onSheetValueChanged: (BottomSheetInfo?) -> Unit,
		private val navigateToLivenessActivity: () -> Unit,
		private val navigateToSelfConfirm: () -> Unit,
		private val onLivenessResultPassed: (result: LivenessResult) -> Unit,
	) : ICameraCaptureListener {
		override fun onLivenessError(e: Throwable) {
			when (e) {
				is ConnectionException -> {
					onSheetValueChanged(BottomSheetInfo(
						title = "?????????????????????? ???????????????? \n????????????????????",
						description = "?????????????????? ?????????????? ?????????????????? \n???? ?????????? ????????????????????",
						buttons = listOf(
							ButtonView.Primary(
								text = "?????????????????? ??????????????",
								composeColor = ComposeColor.composeColor(
									ComposeColors.PrimaryRed
								),
								onClickListener = ButtonView.onClickListener {
									onSheetValueChanged(null)
								}
							)
						)
					))
				}
				is ServerResponseException, is JsonFormatException -> {
					onSheetValueChanged(BottomSheetInfo(
						title = "?????????????? ?????????????????????????? \n????????????????????",
						description = "ServerResponseException ${e.localizedMessage}",
						buttons = listOf(
							ButtonView.Primary(
								text = "?????????????????? ??????????????",
								composeColor = ComposeColor.composeColor(
									ComposeColors.PrimaryRed
								),
								onClickListener = ButtonView.onClickListener {
									navigateToLivenessActivity()
								}
							)
						)
					))
				}
				is CameraException -> {
					onSheetValueChanged(BottomSheetInfo(
						title = "???????????? ???????????? \n?????????????????????????? ????????????????\n ?????? ?????????????? ?? ????????????",
						buttons = listOf(
							ButtonView.Primary(
								text = "?????????????????? ??????????????",
								composeColor = ComposeColor.composeColor(
									ComposeColors.PrimaryRed
								),
								onClickListener = ButtonView.onClickListener {
									navigateToSelfConfirm()
								}
							)
						)
					))
				}
			}
		}

		override fun onLivenessFailed(result: LivenessResult) {
			onSheetValueChanged(BottomSheetInfo(
				title = "?????????????????????????? ???????????????????? ????????",
				buttons = listOf(
					ButtonView.Primary(
						text = "?????????????????? ??????????????",
						composeColor = ComposeColor.composeColor(
							ComposeColors.PrimaryRed
						),
						onClickListener = ButtonView.onClickListener {
							navigateToLivenessActivity()
						}
					)
				)
			))
		}

		override fun onLivenessPassed(result: LivenessResult) = onLivenessResultPassed(result)

		override fun onUpdateOverlay(direction: Direction, hint: Hint) = Unit
	}
}