package kg.optima.mobile.android.ui.features.registration.self_confirm

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.FeatureRouter
import kg.optima.mobile.registration.presentation.self_confirm.SelfConfirmState

object SelfConfirmRouter : FeatureRouter<SelfConfirmState.Model.NavigateTo> {
	@Composable
	override fun compose(stateModel: SelfConfirmState.Model.NavigateTo): Screen {
		return when (stateModel) {
			SelfConfirmState.Model.NavigateTo.DocumentScan -> SelfConfirmScreen
		}
	}
}