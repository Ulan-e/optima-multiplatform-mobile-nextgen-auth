package kg.optima.mobile.android.ui.features.registration.self_confirm

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.base.permission.PermissionController
import kg.optima.mobile.android.ui.features.biometrics.DocumentScanActivity
import kg.optima.mobile.android.ui.features.biometrics.NavigationManager.navigateTo
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.base.presentation.permissions.Permission
import kg.optima.mobile.design_system.android.ui.animation.FadeInAnim
import kg.optima.mobile.design_system.android.ui.animation.FadeInAnimModel
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.text_fields.TitleTextField
import kg.optima.mobile.design_system.android.ui.toolbars.NavigationIcon
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarInfo
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.registration.RegistrationFeatureFactory
import kg.optima.mobile.registration.presentation.self_confirm.IdentificationMode
import kg.optima.mobile.registration.presentation.self_confirm.SelfConfirmIntent
import kg.optima.mobile.registration.presentation.self_confirm.SelfConfirmState
import kg.optima.mobile.resources.Headings
import kotlinx.coroutines.delay
import kz.verigram.veridoc.sdk.VeridocInitializer

object SelfConfirmScreen : Screen {

    @OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val product = remember {
            RegistrationFeatureFactory.create<SelfConfirmIntent, SelfConfirmState>()
        }
        val intent = product.intent
        val state = product.state

        val context = LocalContext.current

        val model by state.stateFlow.collectAsState(initial = State.StateModel.Initial)

        var items by remember { mutableStateOf<List<FadeInAnimModel>>(emptyList()) }
        var buttonEnabled by remember { mutableStateOf(false) }

        val identificationMode by remember { mutableStateOf(IdentificationMode.FULL) }

        when (val selfConfirmStateModel = model) {
            is State.StateModel.Initial -> intent.fadeAnimationModels(identificationMode)
            is SelfConfirmState.SelfConfirmStateModel.AnimationModels ->
                items = selfConfirmStateModel.models.toUiModel()
        }

        LaunchedEffect(key1 = !buttonEnabled) {
            buttonEnabled = when (identificationMode) {
                IdentificationMode.FULL -> {
                    delay(6000)
                    true
                }
                IdentificationMode.SHORT -> {
                    delay(1500)
                    true
                }
            }
        }

        LaunchedEffect(key1 = Unit) {
            buttonEnabled = true
            buttonEnabled = false
        }

        MainContainer(
            mainState = model,
            permissionController = {
                when (it) {
                    PermissionController.State.Accepted -> {
                        VeridocInitializer.init()
                        context.navigateTo(DocumentScanActivity())
                    }
                    is PermissionController.State.DeniedAlways -> {
                        intent.customPermissionRequired(it.permissions)
                    }
                }
            },
            toolbarInfo = ToolbarInfo(
                navigationIcon = NavigationIcon(onBackClick = { })
            ),
            contentHorizontalAlignment = Alignment.Start,
        ) {
            CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(0.dp, 96.dp)
                        )
                        TitleTextField(
                            text = "Подтверждение личности",
                        )
                        Text(
                            modifier = Modifier.padding(top = Deps.Spacing.marginFromTitle),
                            text = "Для прохождении онлайн идентификации необходимо:",
                            fontSize = Headings.H3.sp,
                        )
                        IconTextFields(
                            modifier = Modifier.padding(vertical = Deps.Spacing.standardMargin * 2),
                            items = items,
                        )
                        Spacer(modifier = Modifier.weight(2f))
                    }
                    PrimaryButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        text = "Начать",
                        enabled = buttonEnabled,
                        color = ComposeColors.Green,
                        onClick = {
                            intent.requestPermission(Permission.Camera)
                        },
                    )
                }
            }
        }
    }

    @Composable
    private fun IconTextFields(
        modifier: Modifier = Modifier,
        items: List<FadeInAnimModel>,
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .height(500.dp),
            userScrollEnabled = false,
        ) {
            items(items.size) {
                FadeInAnim(items[it])
            }
        }
    }
}