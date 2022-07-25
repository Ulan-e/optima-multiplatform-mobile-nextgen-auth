package kg.optima.mobile.android.ui.main

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kg.optima.mobile.android.utils.appVersion
import kg.optima.mobile.auth.presentation.intent.AuthIntent
import kg.optima.mobile.auth.presentation.intent.AuthIntentHandler
import kg.optima.mobile.auth.presentation.state.AuthState
import kg.optima.mobile.auth.presentation.state.AuthStateMachine
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.buttons.TransparentButton
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.ComposeColors
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.images.MainImages
import kg.optima.mobile.resources.resId
import org.koin.androidx.compose.get
import org.koin.androidx.compose.inject

@Composable
fun MainScreen(context: Context) {
    val intentHandler: AuthIntentHandler by inject()
    val stateMachine: AuthStateMachine by inject()

    val state by stateMachine.stateFlow.collectAsState(initial = null)

    when (state) {
        is AuthState.Login -> {
            Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
        }
        null -> Unit
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = Deps.standardPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .padding(top = 60.dp)
                .size(width = 170.dp, height = 36.dp),
            painter = painterResource(
                id = MainImages.optimaLogo.resId()
            ),
            contentDescription = "Optima24",
        )
        MainButtonBlock(
            modifier = Modifier.weight(1f),
        )
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Войти",
            onClick = {
                intentHandler.dispatch(AuthIntent.Login("", ""))
            },
        )
        TransparentButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = Deps.standardMargin,
                    bottom = Deps.standardMargin,
                ),
            text = "Зарегистрироваться",
        )
        Text(
            text = "Версия $appVersion",
            fontSize = Headings.H6.px.sp,
            color = ComposeColors.secondaryDescriptionGray,
        )
    }
}