package kg.optima.mobile.android.ui.features.welcome

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.features.biometrics.StartBiometricsActivity
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.android.utils.appVersion
import kg.optima.mobile.common.CommonFeatureFactory
import kg.optima.mobile.common.presentation.welcome.WelcomeIntent
import kg.optima.mobile.common.presentation.welcome.WelcomeState
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.buttons.TransparentButton
import kg.optima.mobile.design_system.android.ui.toolbars.ToolbarInfo
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.Headings


object WelcomeScreen : BaseScreen {

    @Composable
    override fun Content() {
        val product = remember {
            CommonFeatureFactory.create<WelcomeIntent, WelcomeState>()
        }
        val state = product.state
        val intent = product.intent

        val model by state.stateFlow.collectAsState(initial = null)

        val bottomSheetState = remember { mutableStateOf<BottomSheetInfo?>(null) }

        MainContainer(
            mainState = model,
            infoState = bottomSheetState.value,
            toolbarInfo = ToolbarInfo(navigationIcon = null),
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .weight(1f),
            ) {
                Text(
                    text = "Добро пожаловать!",
                    fontSize = Headings.H1.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    modifier = Modifier
                        .padding(top = Deps.Spacing.standardMargin * 2),
                    text = "Весь банк в одном приложении",
                    fontSize = Headings.H4.sp,
                )
            }
            WelcomeScreenButtonBlock(
                modifier = Modifier
                    .wrapContentSize()
                    .weight(3f),
            )
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Войти",
                onClick = { intent.checkIsAuthorized() },
            )
            TransparentButton(
                modifier = Modifier
					.fillMaxWidth()
					.padding(
						top = Deps.Spacing.standardMargin,
						bottom = Deps.Spacing.standardMargin,
					),
                text = "Зарегистрироваться",
                onClick = {
                    intent.register()

//					val intent = Intent(context, StartBiometricsActivity::class.java)
//					context.startActivity(intent)
                },
            )
            Text(
                text = "Версия $appVersion",
                fontSize = Headings.H6.sp,
                color = ComposeColors.DescriptionGray,
            )
        }
    }
}


@Preview
@Composable
private fun WelcomeScreenPreview() {
    WelcomeScreen.Content()
}