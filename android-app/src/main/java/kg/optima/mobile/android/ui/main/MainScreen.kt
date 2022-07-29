package kg.optima.mobile.android.ui.main

import android.util.Log
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kg.optima.mobile.android.ui.login.LoginScreen
import kg.optima.mobile.android.utils.appVersion
import kg.optima.mobile.auth.domain.usecase.login.GrantType
import kg.optima.mobile.auth.presentation.login.LoginFactory
import kg.optima.mobile.auth.presentation.login.LoginIntentHandler
import kg.optima.mobile.auth.presentation.login.LoginStateMachine
import kg.optima.mobile.base.presentation.StateMachine
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.buttons.TransparentButton
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.ComposeColors
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.images.MainImages
import kg.optima.mobile.resources.resId


object MainScreen : Screen {

	@Composable
	override fun Content() {
		val navigator = LocalNavigator.currentOrThrow

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
					navigator.push(LoginScreen)
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
}