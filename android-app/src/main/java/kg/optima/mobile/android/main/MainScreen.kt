package kg.optima.mobile.android.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kg.optima.mobile.auth.domain.AuthUseCase
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.resources.Images
import kg.optima.mobile.resources.resId
import org.koin.androidx.compose.inject

class MainScreen() {
	@Composable
	fun Content() {
		val authUseCase: AuthUseCase by inject()

		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(all = 20.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Image(
				modifier = Modifier
					.padding(top = 60.dp)
					.size(width = 170.dp, height = 36.dp),
				painter = painterResource(
					id = Images.byFileName("logo_optima_horizontal").resId(0)
				),
				contentDescription = "Optima24",
			)
			MainButtonBlock()
			PrimaryButton(
				modifier = Modifier
					.fillMaxWidth()
					.height(50.dp),
				text = "Войти"
			)
		}
	}
}


@Preview
@Composable
private fun MainScreenPreview() = MainScreen().Content()