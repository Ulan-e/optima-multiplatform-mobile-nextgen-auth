package kg.optima.mobile.android.ui.features.common.interview

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.features.FeatureRouter
import kg.optima.mobile.android.ui.features.BottomNavigationScreen
import kg.optima.mobile.registration.presentation.interview.InterviewState

object InterviewRouter : FeatureRouter<InterviewState.Model.NavigateTo> {
	@Composable
	override fun compose(stateModel: InterviewState.Model.NavigateTo): Screen {
		return when (stateModel) {
			InterviewState.Model.NavigateTo.Main -> BottomNavigationScreen
		}
	}
}